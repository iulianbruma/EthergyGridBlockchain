package ro.utcluj.blockchain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producer {
    private String name;
    private String homeAddress;
    private String CNP;
    private List<Integer> baselineConsumption;
    private String accountPrivateKey;
    private String accountAddress;
    private String contractAddress;
}
