package safe.safe.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class ChangPwDTO {


    private String id;
    private String pw;
    private	String pwChk;
    private String pwChk2;


}
