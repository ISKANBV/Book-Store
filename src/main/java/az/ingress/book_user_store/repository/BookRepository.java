package az.ingress.book_user_store.repository;

import az.ingress.book_user_store.domain.Author_;
import az.ingress.book_user_store.domain.Book;
import az.ingress.book_user_store.domain.Book_;
import az.ingress.book_user_store.domain.User_;
import az.ingress.book_user_store.dto.BookSpecDTO;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @EntityGraph(attributePaths = {"publisher", "authors"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Book> findByPublisherId(Long publisherId);

    @EntityGraph(attributePaths = {"publisher", "authors"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Book> findAll();

    @EntityGraph(attributePaths = {"publisher", "authors"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Book> findById(Long id);

    @EntityGraph(attributePaths = {"publisher", "authors"}, type = EntityGraph.EntityGraphType.LOAD)
    default Page<Book> findAllBySpec(BookSpecDTO bookSpecDTO, Pageable pageable) {

        return findAll((root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    if (bookSpecDTO.getBookName().length() > 0) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Book_.NAME)), "%" + bookSpecDTO.getBookName().toLowerCase() + "%"));
                    }

                    if (bookSpecDTO.getBookDescription().length() > 0) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Book_.DESCRIPTION)), "%" + bookSpecDTO.getBookDescription().toLowerCase() + "%"));
                    }

                    if (bookSpecDTO.getPublisherName().length() > 0) {
                        Expression<String> publisherNameConcatExpression = criteriaBuilder.concat(
                                criteriaBuilder.concat(root.get(Book_.PUBLISHER).get(User_.FIRST_NAME),
                                        " "),
                                root.get(Book_.PUBLISHER).get(User_.LAST_NAME));

                        Predicate publisherNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(
                                publisherNameConcatExpression),
                                "%" + bookSpecDTO.getPublisherName().toLowerCase() + "%");

                        predicates.add(publisherNamePredicate);
                    }

                    if (bookSpecDTO.getAuthorName().length() > 0) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join(Book_.AUTHORS).get(Author_.NAME)), "%" + bookSpecDTO.getAuthorName().toLowerCase() + "%"));
                    }

                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Book_.price), bookSpecDTO.getPriceFrom()));

                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(Book_.price), bookSpecDTO.getPriceTo()));
                    Predicate and = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    return and;
                },
                pageable);
    }

}
