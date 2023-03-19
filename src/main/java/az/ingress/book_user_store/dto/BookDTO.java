package az.ingress.book_user_store.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookDTO extends AbstractDTO implements Serializable {

    private static final long serialVersionUID = -4209524908575754744L;
    private Long id;

    @NotEmpty
    @Size(min = 1, max = 255)
    private String name;

    private String description;

    @NotEmpty
    private Set<Long> authorIds;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 5, fraction = 2)
    private BigDecimal price = BigDecimal.ZERO;

}
