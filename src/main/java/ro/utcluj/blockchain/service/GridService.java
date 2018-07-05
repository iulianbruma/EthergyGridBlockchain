package ro.utcluj.blockchain.service;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.RawTransactionManager;
import ro.utcluj.blockchain.contract.ConsumerContract;
import ro.utcluj.blockchain.contract.Ethergy;
import ro.utcluj.blockchain.contract.GridContract;
import ro.utcluj.blockchain.contract.ProducerContract;
import ro.utcluj.blockchain.exception.GridContractDeployException;
import ro.utcluj.blockchain.exception.ServiceException;
import ro.utcluj.blockchain.exception.UserContractDeployException;
import ro.utcluj.blockchain.model.Consumer;
import ro.utcluj.blockchain.model.Producer;
import ro.utcluj.blockchain.model.request.TokenRequest;
import ro.utcluj.blockchain.model.request.UserRequest;
import ro.utcluj.blockchain.model.response.Grid;
import ro.utcluj.blockchain.service.model.DataWallet;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class GridService {
    public static final String DEMAND = "demand";
    public static final String PRODUCTION = "production";

    @Autowired
    private DataWallet dataWallet;
    @Autowired
    private ConsumerService consumerService;
    @Autowired
    private ProducerService producerService;

    public Grid deployContract() {
        if (dataWallet.getGridContractData() != null) {
            throw new GridContractDeployException("The grid is already deployed!");
        }
        Web3j web3j = Web3j.build(new HttpService(dataWallet.getHost()));
        Credentials credentials = null;

        try {
            System.out.println("Account" + dataWallet.getAccount());
            credentials = WalletUtils.loadCredentials(dataWallet.getPassword(), dataWallet.getAccount());
        } catch (Exception e) {
            throw new ServiceException("The credentials could not be loaded", e);
        }
//        long pollingInterval = 3000L;
        int pollingIntervall = 3000;
//        FastRawTransactionManager fastRawTransactionManager = new FastRawTransactionManager(web3j, credentials, new PollingTransactionReceiptProcessor(web3j, pollingInterval, 40));
        RawTransactionManager rawTransactionManager = new RawTransactionManager(web3j, credentials, 200, pollingIntervall);
        StopWatch watch = new StopWatch();
//        RemoteCall<GridContract> contractFuture = GridContract.deploy(web3j, credentials, ManagedTransaction.GAS_PRICE.divide(new BigInteger("5000000")), Contract.GAS_LIMIT);
//        RemoteCall<GridContract> contractFuture = GridContract.deploy(web3j, rawTransactionManager, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
        CompletableFuture<GridContract> contractFuture = GridContract.deploy(web3j, rawTransactionManager, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT).sendAsync();
        GridContract gridContract = null;
        try {
            watch.start();
            gridContract = contractFuture.get();
            watch.stop();

            System.out.println(gridContract.getContractAddress() + " Time grid = " + watch.getTotalTimeSeconds());
        } catch (Exception e) {
            throw new ServiceException("The Grid Contract could not be deployed!", e);
        }

        List<GridContract.EthergyAddressEventResponse> ethergyAddressEvents = gridContract.getEthergyAddressEvents(gridContract.getTransactionReceipt().get());

        if (ethergyAddressEvents.isEmpty()) {
            throw new ServiceException("The Ethergy Contract address is not available. Deploy again the grid contract!");
        }

        String ethergyAddress = ethergyAddressEvents.get(0)._addr;

        Ethergy ethergy = Ethergy.load(ethergyAddress, web3j, rawTransactionManager, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
        checkIfContractIsValid(ethergy);

        Grid grid = new Grid(gridContract.getContractAddress(), ethergyAddress);
        dataWallet.setGridContractData(new ImmutablePair<>(grid, gridContract));
        dataWallet.setEthergyContractData(new ImmutablePair<>(ethergyAddress, ethergy));

        return grid;
    }

    public Consumer registerConsumer(UserRequest userRequest) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        dataWallet.checkIfGridContractIsDeployed();

        String name = userRequest.getName();
        String homeAddress = userRequest.getHomeAddress();
        String CNP = userRequest.getCNP();
        List<Integer> baselineConsumption = userRequest.getBaselineConsumption();

        dataWallet.checkIfUserAlreadyExists(CNP);

        int indexNextConsumer = dataWallet.getConsumerContractMap().size();

        if (indexNextConsumer == dataWallet.getConsumerAccounts().size()) {
            throw new UserContractDeployException("No more contracts for consumers can be deployed");
        }

        String accountPrivateKey = dataWallet.getConsumerAccounts().get(indexNextConsumer);
        String accountAddress = dataWallet.getConsumerAccountsAddress().get(indexNextConsumer);

        System.out.println("Account consumer key = " + accountPrivateKey);
        String gridContractAddress = dataWallet.getGridContractData().getKey().getGridAddress();

        Web3j web3j = Web3j.build(new HttpService(dataWallet.getHost()));
        Credentials credentials = null;

        try {
            credentials = WalletUtils.loadCredentials(dataWallet.getPassword(), accountPrivateKey);
        } catch (Exception e) {
            throw new ServiceException("The credentials could not be loaded", e);
        }

        List<List<Integer>> curves = ListUtils.partition(userRequest.getBaselineConsumption(), 32);

        List<BigInteger> firstBaseline = curves.get(0).stream().map(i -> BigInteger.valueOf(i.intValue())).collect(Collectors.toList());
        List<BigInteger> secondBaseline = curves.get(1).stream().map(i -> BigInteger.valueOf(i.intValue())).collect(Collectors.toList());
        List<BigInteger> thirdBaseline = curves.get(2).stream().map(i -> BigInteger.valueOf(i.intValue())).collect(Collectors.toList());

//        GridContract gridContract = GridContract.load(gridContractAddress, web3j, credentials, ManagedTransaction.GAS_PRICE.divide(new BigInteger("5000000")), BigInteger.valueOf(4300000000L));
//        long pollingInterval = 3000L;
        int pollingInterval = 3000;
//        FastRawTransactionManager fastRawTransactionManager = new FastRawTransactionManager(web3j, credentials, new PollingTransactionReceiptProcessor(web3j, pollingInterval, 40));
        RawTransactionManager rawTransactionManager = new RawTransactionManager(web3j, credentials, 200, pollingInterval);

        GridContract gridContract = GridContract.load(gridContractAddress, web3j, rawTransactionManager, ManagedTransaction.GAS_PRICE.divide(new BigInteger("5000000")), Contract.GAS_LIMIT);
        checkIfContractIsValid(gridContract);

        CompletableFuture<TransactionReceipt> receiptCompletableFuture = null;
        TransactionReceipt transactionReceipt = null;
        StopWatch watch = new StopWatch();
        try {
            watch.start();
            receiptCompletableFuture = gridContract.registerConsumer(name, homeAddress, CNP, firstBaseline, secondBaseline, thirdBaseline).sendAsync();
//            transactionReceipt = gridContract.registerConsumer(name, homeAddress, CNP, new BigInteger("34"), firstBaseline, secondBaseline, thirdBaseline).send();
//            gridContract.updateDemandAndBalanceCurve(firstBaseline, secondBaseline, thirdBaseline).sendAsync();

        } catch (Exception e) {
            throw new UserContractDeployException("Could not register a consumer", e);
        }

        try {
            transactionReceipt = receiptCompletableFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            gridContract.updateDemandAndBalanceCurve(firstBaseline, secondBaseline, thirdBaseline).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        watch.stop();
        System.out.println("Time consumer register = " + watch.getTotalTimeSeconds());

        List<GridContract.GenerateAddressEventResponse> generateAddressEvent = gridContract.getGenerateAddressEvents(transactionReceipt);

        if (generateAddressEvent.isEmpty()) {
            throw new ServiceException("The Consumer Contract address is not available. Register again the consumer!");
        }

        String consumerContractAddress = generateAddressEvent.get(0)._address;
        System.out.println("Msg.sender = " + generateAddressEvent.get(0).msg_sender);

//        ConsumerContract consumerContract = ConsumerContract.load(consumerContractAddress, web3j, credentials, ManagedTransaction.GAS_PRICE.divide(new BigInteger("100000")), Contract.GAS_LIMIT);
        ConsumerContract consumerContract = ConsumerContract.load(consumerContractAddress, web3j, rawTransactionManager, ManagedTransaction.GAS_PRICE.divide(new BigInteger("100000")), Contract.GAS_LIMIT);

        checkIfContractIsValid(consumerContract);

        Consumer consumer = new Consumer(name, homeAddress, CNP, baselineConsumption, getConsumerCurve(consumerContract), accountPrivateKey, accountAddress, consumerContractAddress);

        dataWallet.getConsumerContractMap().put(CNP, new ImmutablePair<>(consumer, consumerContract));

        stopWatch.stop();
        System.out.println("Consumer TOTAL = " + stopWatch.getTotalTimeSeconds());
        return consumer;
    }

    public Producer registerProducer(UserRequest userRequest) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        dataWallet.checkIfGridContractIsDeployed();

        String name = userRequest.getName();
        String homeAddress = userRequest.getHomeAddress();
        String CNP = userRequest.getCNP();
        List<Integer> baselineConsumption = userRequest.getBaselineConsumption();

        dataWallet.checkIfUserAlreadyExists(CNP);

        int indexNextProducer = dataWallet.getProducerContractMap().size();

        if (indexNextProducer == dataWallet.getProducerAccounts().size()) {
            throw new UserContractDeployException("No more contracts for producers can be deployed");
        }

        String accountPrivateKey = dataWallet.getProducerAccounts().get(indexNextProducer);
        String accountAddress = dataWallet.getProducerAccountsAddress().get(indexNextProducer);

        String gridContractAddress = dataWallet.getGridContractData().getKey().getGridAddress();

        Web3j web3j = Web3j.build(new HttpService(dataWallet.getHost()));
        Credentials credentials = null;

        try {
            credentials = WalletUtils.loadCredentials(dataWallet.getPassword(), accountPrivateKey);
        } catch (Exception e) {
            throw new ServiceException("The credentials could not be loaded", e);
        }

        List<List<Integer>> curves = ListUtils.partition(userRequest.getBaselineConsumption(), 32);

        List<BigInteger> firstBaseline = curves.get(0).stream().map(i -> BigInteger.valueOf(i.intValue())).collect(Collectors.toList());
        List<BigInteger> secondBaseline = curves.get(1).stream().map(i -> BigInteger.valueOf(i.intValue())).collect(Collectors.toList());
        List<BigInteger> thirdBaseline = curves.get(2).stream().map(i -> BigInteger.valueOf(i.intValue())).collect(Collectors.toList());

//        GridContract gridContract = GridContract.load(gridContractAddress, web3j, credentials, ManagedTransaction.GAS_PRICE.divide(new BigInteger("5000000")), BigInteger.valueOf(4300000000L));
//        long pollingInterval = 3000L;
        int pollingInterval = 3000;
//        FastRawTransactionManager fastRawTransactionManager = new FastRawTransactionManager(web3j, credentials, new PollingTransactionReceiptProcessor(web3j, pollingInterval, 40));
        RawTransactionManager rawTransactionManager = new RawTransactionManager(web3j, credentials, 200, pollingInterval);

        GridContract gridContract = GridContract.load(gridContractAddress, web3j, rawTransactionManager, ManagedTransaction.GAS_PRICE.divide(new BigInteger("5000000")), Contract.GAS_LIMIT);
        checkIfContractIsValid(gridContract);

        CompletableFuture<TransactionReceipt> receiptCompletableFuture = null;
        TransactionReceipt transactionReceipt = null;
        StopWatch watch = new StopWatch();
        try {
            watch.start();
            receiptCompletableFuture = gridContract.registerProducer(name, homeAddress, CNP, firstBaseline, secondBaseline, thirdBaseline).sendAsync();
//            transactionReceipt = gridContract.registerConsumer(name, homeAddress, CNP, new BigInteger("34"), firstBaseline, secondBaseline, thirdBaseline).send();
//            gridContract.updateDemandAndBalanceCurve(firstBaseline, secondBaseline, thirdBaseline).sendAsync();

        } catch (Exception e) {
            throw new UserContractDeployException("Could not register a consumer", e);
        }

        try {
            transactionReceipt = receiptCompletableFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            gridContract.updateProductionAndBalanceCurve(firstBaseline, secondBaseline, thirdBaseline).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        watch.stop();
        System.out.println("Time producer register = " + watch.getTotalTimeSeconds());

        List<GridContract.GenerateAddressEventResponse> generateAddressEvent = gridContract.getGenerateAddressEvents(transactionReceipt);

        if (generateAddressEvent.isEmpty()) {
            throw new ServiceException("The Producer Contract address is not available. Register again the consumer!");
        }

        String producerContractAddress = generateAddressEvent.get(0)._address;

//        ConsumerContract consumerContract = ConsumerContract.load(producerContractAddress, web3j, credentials, ManagedTransaction.GAS_PRICE.divide(new BigInteger("100000")), Contract.GAS_LIMIT);
        ProducerContract producerContract = ProducerContract.load(producerContractAddress, web3j, rawTransactionManager, ManagedTransaction.GAS_PRICE.divide(new BigInteger("100000")), Contract.GAS_LIMIT);

        checkIfContractIsValid(producerContract);

        Producer producer = new Producer(name, homeAddress, CNP, baselineConsumption, accountPrivateKey, accountAddress, producerContractAddress);

        dataWallet.getProducerContractMap().put(CNP, new ImmutablePair<>(producer, producerContract));

        stopWatch.stop();
        System.out.println("Consumer TOTAL = " + stopWatch.getTotalTimeSeconds());
        return producer;
    }

    public Integer getPricePerkWh() {
        dataWallet.checkIfGridContractIsDeployed();

        BigInteger price = null;
        try {
            price = dataWallet.getGridContractData().getValue().getPricePerkWh().send();
        } catch (Exception e) {
            new ServiceException("The price per kWh could not be retrieved from Grid Contract", e);
        }
        return price.intValue();
    }

    public Integer addToken(TokenRequest tokenRequest) {
        dataWallet.checkIfGridContractIsDeployed();
        String CNP = tokenRequest.getCNP();
        Integer amount = tokenRequest.getAmount();

        dataWallet.checkIfUserExists(CNP);
        GridContract gridContract = dataWallet.getGridContractData().getValue();

        try {
            gridContract.giveToken(dataWallet.getContractAddressByCNP(CNP), BigInteger.valueOf(amount)).send();
        } catch (Exception e) {
            throw new ServiceException("Could not allocate token for CNP = " + CNP, e);
        }

        return amount;
    }

    public Integer getBalance(String CNP) {
        dataWallet.checkIfGridContractIsDeployed();
        dataWallet.checkIfUserExists(CNP);

        Ethergy ethergy = dataWallet.getEthergyContractData().getValue();
//        String accountAddress = dataWallet.get
//        GridContract gridContract = dataWallet.getGridContractData().getValue();
        BigInteger balance;
        try {
            //System.out.println(ethergy.getUserBalance(address).send().intValue());
//            balance = gridContract.getUserBalance(address).send();
//            balance = ethergy.getUserBalance(address).send();
        } catch (Exception e) {
            throw new ServiceException("Could not get the balance for user CNP = " + CNP, e);
        }

        return null;
    }

    private Ethergy getEthergyContract() {
        Web3j web3j = Web3j.build(new HttpService(dataWallet.getHost()));
        Credentials credentials = null;

        try {
            System.out.println("Account" + dataWallet.getAccount());
            credentials = WalletUtils.loadCredentials(dataWallet.getPassword(), dataWallet.getAccount());
        } catch (Exception e) {
            throw new ServiceException("The credentials could not be loaded", e);
        }
//        long pollingInterval = 3000L;
        int pollingIntervall = 3000;
//        FastRawTransactionManager fastRawTransactionManager = new FastRawTransactionManager(web3j, credentials, new PollingTransactionReceiptProcessor(web3j, pollingInterval, 40));
        RawTransactionManager rawTransactionManager = new RawTransactionManager(web3j, credentials, 200, pollingIntervall);

        Ethergy ethergy = Ethergy.load(dataWallet.getGridContractData().getKey().getEthergyAddress(), web3j, rawTransactionManager, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);

        checkIfContractIsValid(ethergy);

        return ethergy;
    }

    public List<Integer> getBalanceCurve() {
        dataWallet.checkIfGridContractIsDeployed();

        return getBalanceCurveFromGrid();
    }

    public List<Integer> getDemandCurve() {
        dataWallet.checkIfGridContractIsDeployed();

        return getCurve(DEMAND);
    }

    public List<Integer> getProductionCurve() {
        dataWallet.checkIfGridContractIsDeployed();

        return getCurve(PRODUCTION);
    }

    public void startSimulation() {
        dataWallet.checkIfGridContractIsDeployed();

        List<Consumer> consumers = dataWallet.getConsumerContractMap().values().stream().map(e -> e.getKey()).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(consumers)) {
            System.out.println("There are no consumers!");
            return;
        }

        GridContract gridContract = dataWallet.getGridContractData().getValue();

        try {
            gridContract.calculateBalanceCurve().send();
            System.out.println("The balance curve was calculated");
        } catch (Exception e) {
            throw new ServiceException("Could not calculate the balance curve!", e);
        }
//        List<CompletableFuture<TransactionReceipt>> completableFutures = new ArrayList<>();

        consumers.forEach(e -> {
            List<List<Integer>> curves = ListUtils.partition(e.getBaselineConsumption(), 32);
            List<BigInteger> firstBaseline = curves.get(0).stream().map(i -> BigInteger.valueOf(i.intValue())).collect(Collectors.toList());
            List<BigInteger> secondBaseline = curves.get(1).stream().map(i -> BigInteger.valueOf(i.intValue())).collect(Collectors.toList());
            List<BigInteger> thirdBaseline = curves.get(2).stream().map(i -> BigInteger.valueOf(i.intValue())).collect(Collectors.toList());

            try {
                gridContract.setCurveForConsumer(e.getContractAddress(), true, firstBaseline, secondBaseline, thirdBaseline).send();
                System.out.println("Curve for consumer " + e.getCNP() + " set up!");
            } catch (Exception e1) {
                throw new ServiceException("Could not set curves for all the consumers!", e1);
            }
        });

        /*completableFutures.forEach(e -> {
            try {
                e.get();
            }  catch (Exception e1) {
                throw new ServiceException("Could not set curves for all the consumers!", e1);
            }
        });*/

    }

    private <T extends Contract> void checkIfContractIsValid(T contract) {
        try {
            if (contract == null || !contract.isValid()) {
                throw new UserContractDeployException("Could not load the Grid Contract");
            }
        } catch (IOException e) {
            throw new UserContractDeployException("Could not load the Grid Contract");
        }
    }

    private List<Integer> getConsumerCurve(ConsumerContract contract) {
        List<Uint256> curve;
        try {
            curve = contract.getCurve().send();
        } catch (Exception e) {
            System.out.println("Could not get the curve for the consumer");
            return null;
        }

        return curve.stream().map(i -> Integer.valueOf(String.valueOf(i.getValue()))).collect(Collectors.toList());
    }

    private List<Integer> getCurve(String type) {
        List<Integer> curve = new ArrayList<>();
        List<Uint256> first = null;
        List<Uint256> second = null;
        List<Uint256> third = null;

        GridContract contract = dataWallet.getGridContractData().getValue();

        try {
            if (type.equals(DEMAND)) {
                first = contract.getFirstDemandCurve().send();
                second = contract.getSecondDemandCurve().send();
                third = contract.getThirdDemandCurve().send();
            } else if (type.equals(PRODUCTION)) {
                first = contract.getFirstProductionCurve().send();
                second = contract.getSecondProductionCurve().send();
                third = contract.getThirdProductionCurve().send();
            }
        } catch (Exception e) {
            throw new ServiceException("The " + type.toLowerCase() + " curve could not be retrieved from the blockchain", e);
        }

        if (first == null || second == null || third == null) {
            throw new ServiceException("The " + type.toLowerCase() + " curve could not be retrieved from the blockchain");
        }

        curve.addAll(first.stream().map(e -> e.getValue().intValue()).collect(Collectors.toList()));
        curve.addAll(second.stream().map(e -> e.getValue().intValue()).collect(Collectors.toList()));
        curve.addAll(third.stream().map(e -> e.getValue().intValue()).collect(Collectors.toList()));

        return curve;
    }

    private List<Integer> getBalanceCurveFromGrid() {
        List<Integer> curve = new ArrayList<>();
        List<Int256> first;
        List<Int256> second;
        List<Int256> third;

        GridContract contract = dataWallet.getGridContractData().getValue();

        try {
            first = contract.getFirstBalanceCurve().send();
            second = contract.getSecondBalanceCurve().send();
            third = contract.getThirdBalanceCurve().send();
        } catch (Exception e) {
            throw new ServiceException("The balance curve could not be retrieved from the blockchain", e);
        }

        if (first == null || second == null || third == null) {
            throw new ServiceException("The base curve could not be retrieved from the blockchain");
        }

        curve.addAll(first.stream().map(e -> e.getValue().intValue()).collect(Collectors.toList()));
        curve.addAll(second.stream().map(e -> e.getValue().intValue()).collect(Collectors.toList()));
        curve.addAll(third.stream().map(e -> e.getValue().intValue()).collect(Collectors.toList()));

        return curve;
    }

}
