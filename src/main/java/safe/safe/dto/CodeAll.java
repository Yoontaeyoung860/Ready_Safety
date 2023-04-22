package safe.safe.dto;

import lombok.Data;

@Data
public class CodeAll {
    private String pcode;     //부모 코드
    private String pdecode;   //부모 디코드
    private String ccode;     //자식 코드
    private String cdecode;   //자식 디코드
}
