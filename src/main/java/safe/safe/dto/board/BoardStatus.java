package safe.safe.dto.board;

public enum BoardStatus {


  D("삭제"),I("임시저장"),W("경고");

  private final String description;

  BoardStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
