package ro.utcluj.blockchain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Consumer {
    private String name;
    private String homeAddress;
    private String CNP;
    private List<Integer> baselineConsumption;
    private List<Integer> curve;
    private String accountPrivateKey;
    private String accountAddress;
    private String contractAddress;
}
