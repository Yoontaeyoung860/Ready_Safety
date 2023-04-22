package safe.safe.paging;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter
@ToString
public class FindCriteria extends PageCriteria {

  private String searchType;
  private String keyword;

  public FindCriteria(RecordCriteria rc, int pageCount) {
    super(rc, pageCount);
  }
}
