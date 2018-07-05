package ro.utcluj.blockchain.service;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import ro.utcluj.blockchain.contract.ConsumerContract;
import ro.utcluj.blockchain.contract.Ethergy;
import ro.utcluj.blockchain.exception.ServiceException;
import ro.utcluj.blockchain.exception.UserNotFoundException;
import ro.utcluj.blockchain.model.Consumer;
import ro.utcluj.blockchain.model.request.ValueRequest;
import ro.utcluj.blockchain.model.response.ValueResponse;
import ro.utcluj.blockchain.service.model.DataWallet;

import javax.validation.constraints.NotBlank;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ConsumerService {

    @Autowired
    private DataWallet dataWallet;

    public Consumer getConsumer(@NotBlank String CNP) {
        if (!dataWallet.getConsumerContractMap().containsKey(CNP)) {
            throw new UserNotFoundException("No customer was found for CNP = " + CNP);
        }

        return dataWallet.getConsumerContractMap().get(CNP).getKey();
    }

    public List<Consumer> getAllConsumer() {
        if (dataWallet.getConsumerContractMap().isEmpty()) {
            return Collections.emptyList();
        }

        return dataWallet.getConsumerContractMap().values().stream().map(e -> e.getKey()).collect(Collectors.toList());
    }

    public ValueResponse checkValue(ValueRequest valueRequest) {
        String CNP = valueRequest.getCNP();

        dataWallet.checkIfUserExists(CNP);

        ConsumerContract consumerContract = dataWallet.getConsumerContractMap().get(CNP).getValue();

        CompletableFuture<TransactionReceipt> completableFuture = consumerContract.checkValue(new BigInteger(String.valueOf(valueRequest.getValue())), new BigInteger(String.valueOf(valueRequest.getGeneralIndex()))).sendAsync();
        TransactionReceipt transactionReceipt = null;
        try {
            transactionReceipt = completableFuture.get();
        } catch (Exception e) {
            throw new ServiceException("Could not check value for consumer contract!");
        }

        List<ConsumerContract.OkValueEventResponse> okValueEventResponses = consumerContract.getOkValueEvents(transactionReceipt);
        List<ConsumerContract.DeviationOccuredEventResponse> deviationOccuredEventResponses = consumerContract.getDeviationOccuredEvents(transactionReceipt);

        if (valueRequest.getGeneralIndex() % 8 == 7) {
            Consumer consumer = dataWallet.getConsumerContractMap().get(CNP).getKey();
            List<List<Integer>> curves = ListUtils.partition(consumer.getBaselineConsumption(), 32);
            List<BigInteger> firstBaseline = curves.get(0).stream().map(i -> BigInteger.valueOf(i.intValue())).collect(Collectors.toList());
            List<BigInteger> secondBaseline = curves.get(1).stream().map(i -> BigInteger.valueOf(i.intValue())).collect(Collectors.toList());
            List<BigInteger> thirdBaseline = curves.get(2).stream().map(i -> BigInteger.valueOf(i.intValue())).collect(Collectors.toList());
            try {
                dataWallet.getGridContractData().getValue().setCurveForConsumer(consumer.getContractAddress(), false, firstBaseline, secondBaseline, thirdBaseline).send();
            } catch (Exception e) {
                throw new ServiceException("Could not set a new curve for the consumer!", e);
            }
        }

        return getValueResponse(okValueEventResponses, deviationOccuredEventResponses);
    }

    public List<Integer> getCurve(@NotBlank String CNP) {
        dataWallet.checkIfUserExists(CNP);

        ConsumerContract consumerContract = dataWallet.getConsumerContractMap().get(CNP).getValue();

        CompletableFuture<List> completableFuture = consumerContract.getCurve().sendAsync();
        List<Uint256> curveUint;
        try {
            curveUint = completableFuture.get();
        } catch (Exception e) {
            throw new ServiceException("Could not check value for consumer contract!");
        }

        List<Integer> curve = curveUint.stream().map(e -> e.getValue().intValue()).collect(Collectors.toList());

        return curve;
    }

    public List<Integer> getBaselineConsumptionCurve(@NotBlank String CNP) {
        dataWallet.checkIfUserExists(CNP);

        return dataWallet.getConsumerContractMap().get(CNP).getKey().getBaselineConsumption();
    }

    public Integer getBalance(String CNP) {
        dataWallet.checkIfUserExists(CNP);

        Ethergy ethergy = dataWallet.getEthergyContractData().getValue();
        String accountAddress = dataWallet.getConsumerContractMap().get(CNP).getKey().getAccountAddress();

        ConsumerContract consumerContract = dataWallet.getConsumerContractMap().get(CNP).getValue();
        System.out.println("Consumer private key\n" + dataWallet.getConsumerContractMap().get(CNP).getKey().getAccountPrivateKey());
        BigInteger balance;
        try {
            String address = consumerContract.getOwnerAdress().send();
            System.out.println("Address account = " + address + "; match = " + address.equalsIgnoreCase(accountAddress));
            balance = ethergy.getUserBalance(accountAddress).send();
        } catch (Exception e) {
            throw new ServiceException("Could not get the balance for consumer CNP = " + CNP, e);
        }

        return balance.intValue();
    }

    /*public List<Integer> getBaselineConsumptionCurveGrid(@NotBlank String CNP) {
        dataWallet.checkIfUserAlreadyExists(CNP);

        ConsumerContract consumerContract = dataWallet.getConsumerContractMap().get(CNP).getValue();

        CompletableFuture<List> completableFuture1 = consumerContract.getFirstBaselineConsumptionCurve().sendAsync();
        CompletableFuture<List> completableFuture2 = consumerContract.getSecondBaselineConsumptionCurve().sendAsync();
        CompletableFuture<List> completableFuture3 = consumerContract.getThirdBaselineConsumptionCurve().sendAsync();

        List<Uint256> curveUint1;
        List<Uint256> curveUint2;
        List<Uint256> curveUint3;

        try {
            curveUint1 = completableFuture1.get();
            curveUint2 = completableFuture2.get();
            curveUint3 = completableFuture3.get();
        } catch (Exception e) {
            throw new ServiceException("Could not check value for consumer contract!");
        }

        List<Integer> baselineCurve = new ArrayList<>();
        baselineCurve.addAll(curveUint1.stream().map(e -> e.getValue().intValue()).collect(Collectors.toList()));
        baselineCurve.addAll(curveUint2.stream().map(e -> e.getValue().intValue()).collect(Collectors.toList()));
        baselineCurve.addAll(curveUint3.stream().map(e -> e.getValue().intValue()).collect(Collectors.toList()));

        return baselineCurve;
    }*/

    private ValueResponse getValueResponse(List<ConsumerContract.OkValueEventResponse> okValueEventResponses, List<ConsumerContract.DeviationOccuredEventResponse> deviationOccuredEventResponses) {
        if (okValueEventResponses.isEmpty() && deviationOccuredEventResponses.isEmpty()) {
            throw new ServiceException("No deviation or OK event was triggered!");
        }

        ValueResponse valueResponse = new ValueResponse();
        if (!okValueEventResponses.isEmpty()) {
            ConsumerContract.OkValueEventResponse response = okValueEventResponses.get(0);

            valueResponse.setDeviationOccurred(false);
            valueResponse.setIndex(response._index.intValue());
            valueResponse.setIdealValue(response.requiredValue.intValue());
            valueResponse.setRealValue(response.realValue.intValue());
            return valueResponse;
        }

        ConsumerContract.DeviationOccuredEventResponse response = deviationOccuredEventResponses.get(0);

        valueResponse.setDeviationOccurred(true);
        valueResponse.setIndex(response._index.intValue());
        valueResponse.setIdealValue(response.requiredValue.intValue());
        valueResponse.setRealValue(response.realValue.intValue());

        return valueResponse;
    }

}
