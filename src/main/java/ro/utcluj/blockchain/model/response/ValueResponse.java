package ro.utcluj.blockchain.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValueResponse {
    private int index;
    private int idealValue;
    private int realValue;
    private boolean deviationOccurred;
}
