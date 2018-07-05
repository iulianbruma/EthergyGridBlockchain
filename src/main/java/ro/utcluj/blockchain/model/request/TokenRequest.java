package ro.utcluj.blockchain.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequest {
    @NotNull
    private Integer amount;
    @NotBlank
    private String CNP;
}
