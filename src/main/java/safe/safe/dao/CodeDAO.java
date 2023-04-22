package safe.safe.dao;

import safe.safe.dto.Code;
import safe.safe.dto.CodeAll;

import java.util.List;

public interface CodeDAO {
    /**
     * 상위코드를 입력하면 하위코드 반환
     * @param pcode 상위코드
     * @return 하위코드&디코드
     */
    List<Code> code(String pcode);

    /**
     * 모든 코드 반환
     * @return
     */
    List<CodeAll> codeAll();
}
