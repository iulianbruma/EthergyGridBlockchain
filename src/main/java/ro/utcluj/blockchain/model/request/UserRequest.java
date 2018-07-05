package ro.utcluj.blockchain.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String homeAddress;
    @NotBlank
    private String CNP;
    @NotNull
    private List<Integer> baselineConsumption;
}
