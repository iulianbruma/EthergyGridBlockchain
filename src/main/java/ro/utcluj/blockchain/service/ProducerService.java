package ro.utcluj.blockchain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import ro.utcluj.blockchain.contract.ConsumerContract;
import ro.utcluj.blockchain.contract.Ethergy;
import ro.utcluj.blockchain.contract.ProducerContract;
import ro.utcluj.blockchain.exception.ServiceException;
import ro.utcluj.blockchain.exception.UserNotFoundException;
import ro.utcluj.blockchain.model.Producer;
import ro.utcluj.blockchain.model.request.ValueRequest;
import ro.utcluj.blockchain.model.response.ValueResponse;
import ro.utcluj.blockchain.service.model.DataWallet;

import javax.validation.constraints.NotBlank;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ProducerService {

    @Autowired
    private DataWallet dataWallet;

    public Producer getProducer(String CNP) {
        if (!dataWallet.getProducerContractMap().containsKey(CNP)) {
            throw new UserNotFoundException("No producer was found for CNP = " + CNP);
        }

        return dataWallet.getProducerContractMap().get(CNP).getKey();
    }

    public List<Producer> getAllProducer() {
        if (dataWallet.getProducerContractMap().isEmpty()) {
            return Collections.emptyList();
        }

        return dataWallet.getProducerContractMap().values().stream().map(e -> e.getKey()).collect(Collectors.toList());
    }

    public List<Integer> getBaselineConsumptionCurve(@NotBlank String CNP) {
        dataWallet.checkIfUserExists(CNP);

        return dataWallet.getProducerContractMap().get(CNP).getKey().getBaselineConsumption();
    }

    public Integer getBalance(String CNP) {
        dataWallet.checkIfUserExists(CNP);

        Ethergy ethergy = dataWallet.getEthergyContractData().getValue();
        String accountAddress = dataWallet.getProducerContractMap().get(CNP).getKey().getAccountAddress();

        System.out.println("Producer private key\n" + dataWallet.getProducerContractMap().get(CNP).getKey().getAccountPrivateKey());
        BigInteger balance;
        try {
            balance = ethergy.getUserBalance(accountAddress).send();
        } catch (Exception e) {
            throw new ServiceException("Could not get the balance for producer CNP = " + CNP, e);
        }

        return balance.intValue();
    }

    public ValueResponse registerProduction(ValueRequest valueRequest) {
        String CNP = valueRequest.getCNP();

        dataWallet.checkIfUserExists(CNP);

        ProducerContract producerContract = dataWallet.getProducerContractMap().get(CNP).getValue();

        CompletableFuture<TransactionReceipt> completableFuture = producerContract.registerProduction(new BigInteger(String.valueOf(valueRequest.getValue())), new BigInteger(String.valueOf(valueRequest.getGeneralIndex()))).sendAsync();
        TransactionReceipt transactionReceipt = null;
        try {
            transactionReceipt = completableFuture.get();
        } catch (Exception e) {
            throw new ServiceException("Could not register production for producer contract!");
        }

        List<ProducerContract.OkValueEventResponse> okValueEventResponses = producerContract.getOkValueEvents(transactionReceipt);
        List<ProducerContract.DeviationOccuredEventResponse> deviationOccuredEventResponses = producerContract.getDeviationOccuredEvents(transactionReceipt);

        return getValueResponse(okValueEventResponses, deviationOccuredEventResponses);
    }

    private ValueResponse getValueResponse(List<ProducerContract.OkValueEventResponse> okValueEventResponses, List<ProducerContract.DeviationOccuredEventResponse> deviationOccuredEventResponses) {
        if (okValueEventResponses.isEmpty() && deviationOccuredEventResponses.isEmpty()) {
            throw new ServiceException("No deviation or OK event was triggered!");
        }

        ValueResponse valueResponse = new ValueResponse();
        if (!okValueEventResponses.isEmpty()) {
            ProducerContract.OkValueEventResponse response = okValueEventResponses.get(0);

            valueResponse.setDeviationOccurred(false);
            valueResponse.setIndex(response._index.intValue());
            valueResponse.setIdealValue(response.requiredValue.intValue());
            valueResponse.setRealValue(response.realValue.intValue());
            return valueResponse;
        }

        ProducerContract.DeviationOccuredEventResponse response = deviationOccuredEventResponses.get(0);

        valueResponse.setDeviationOccurred(true);
        valueResponse.setIndex(response._index.intValue());
        valueResponse.setIdealValue(response.requiredValue.intValue());
        valueResponse.setRealValue(response.realValue.intValue());

        return valueResponse;
    }
}
