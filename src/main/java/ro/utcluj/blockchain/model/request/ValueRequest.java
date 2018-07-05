package ro.utcluj.blockchain.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValueRequest {
    @NotNull
    private Integer value;
    @NotNull
    private Integer generalIndex;
    @NotBlank
    private String CNP;
}
