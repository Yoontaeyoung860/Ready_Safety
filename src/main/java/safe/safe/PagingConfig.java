package safe.safe;

import safe.safe.paging.FindCriteria;
import safe.safe.paging.RecordCriteria;
import safe.safe.paging.PageCriteria;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PagingConfig {

  private static final  int REC_COUNT_10_PER_PAGE = 10;
  private static final  int PAGE_COUNT_10_PER_PAGE = 10;

  private static final  int REC_COUNT_5_PER_PAGE = 5;
  private static final  int PAGE_COUNT_5_PER_PAGE = 5;

  //레코드 10단위
  @Bean
  public RecordCriteria rc10(){
    return new RecordCriteria(REC_COUNT_10_PER_PAGE);
  }

  //페이지 10단위
  @Bean
  public PageCriteria pc10(){
    return new PageCriteria(rc10(), PAGE_COUNT_10_PER_PAGE);
  }

  //레코드 5단위
  @Bean
  public RecordCriteria rc5(){
    return new RecordCriteria(REC_COUNT_5_PER_PAGE);
  }

  //페이지 5단위
  @Bean
  public PageCriteria pc5(){
    return new PageCriteria(rc10(), PAGE_COUNT_5_PER_PAGE);
  }

  //FindCriteria 는 PageCriteria 를 상속함
  
  //페이지 10단위
  @Bean
  public FindCriteria fc10() {
    return new FindCriteria(rc10(),PAGE_COUNT_10_PER_PAGE);
  }

  //페이지 5단위
  @Bean
  public FindCriteria fc5() {
    return  new FindCriteria(rc5(),PAGE_COUNT_5_PER_PAGE);
  }
}
