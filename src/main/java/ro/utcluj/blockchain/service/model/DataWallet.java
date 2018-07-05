package ro.utcluj.blockchain.service.model;

import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ro.utcluj.blockchain.contract.ConsumerContract;
import ro.utcluj.blockchain.contract.Ethergy;
import ro.utcluj.blockchain.contract.GridContract;
import ro.utcluj.blockchain.contract.ProducerContract;
import ro.utcluj.blockchain.exception.GridContractDeployException;
import ro.utcluj.blockchain.exception.UserContractDeployException;
import ro.utcluj.blockchain.model.Consumer;
import ro.utcluj.blockchain.model.Producer;
import ro.utcluj.blockchain.model.response.Grid;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "grid")
@Data
public class DataWallet {
    private String host;
    private String account;
    private String password;
    private List<String> consumerAccounts;
    private List<String> consumerAccountsAddress;
    private List<String> producerAccounts;
    private List<String> producerAccountsAddress;
    private Pair<String, Ethergy> ethergyContractData;
    private Pair<Grid, GridContract> gridContractData;
    private Map<String, Pair<Consumer, ConsumerContract>> consumerContractMap = new HashMap<>();
    private Map<String, Pair<Producer, ProducerContract>> producerContractMap = new HashMap<>();

    public void checkIfGridContractIsDeployed() {
        if (this.getGridContractData() == null) {
            throw new GridContractDeployException("The Grid Contract has to be deployed first!");
        }
    }

    public void checkIfUserAlreadyExists(String CNP) {
        if (this.getConsumerContractMap().containsKey(CNP) || this.getProducerContractMap().containsKey(CNP)) {
            throw new UserContractDeployException("This customer was already deployed having CNP = " + CNP);
        }
        if (this.getProducerContractMap().containsKey(CNP)) {
            throw new UserContractDeployException("This producer was already deployed having CNP = " + CNP);
        }
    }

    public void checkIfUserExists(String CNP) {
        if (!this.getConsumerContractMap().containsKey(CNP) && !this.getProducerContractMap().containsKey(CNP)) {
            throw new UserContractDeployException("This user was NOT deployed yet with CNP = " + CNP);
        }
    }

    public String getContractAddressByCNP(String CNP) {
        if (this.getConsumerContractMap().containsKey(CNP)) {
            return this.getConsumerContractMap().get(CNP).getKey().getContractAddress();
        }

        return this.getProducerContractMap().get(CNP).getKey().getContractAddress();
    }

    public boolean isConsumerCNP(String CNP) {
        return getConsumerContractMap().containsKey(CNP);
    }

    public BigInteger getGeneralIndex() {
        Calendar now = Calendar.getInstance();
        int hours = now.get(Calendar.HOUR_OF_DAY);
        int minutes = now.get(Calendar.MINUTE);
        int index = hours * 4 + minutes / 15;
        System.out.println("Hour = " + hours + " minutes = " + minutes + " generalIndex " + index);
        System.out.println(new BigInteger(String.valueOf(index)).intValue());
        return new BigInteger(String.valueOf(index));
    }

}
