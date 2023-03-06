package az.ingress.book_user_store.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookSpecDTO {
    private String bookName;
    private String bookDescription;
    private String authorName;
    private String publisherName;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
}
