package safe.safe.web.controller.member.pwutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PasswordGenerator {
  private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
  private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String DIGITS = "0123456789";
  private static final String PUNCTUATION = "!@#$%&*()_+-=[]|,./?><";
  private boolean useLower;
  private boolean useUpper;
  private boolean useDigits;
  private boolean usePunctuation;

  private PasswordGenerator() {
    throw new UnsupportedOperationException("디폴트 생성자 지원 안함!");
  }

  private PasswordGenerator(PasswordGeneratorBuilder builder) {
    this.useLower = builder.useLower;
    this.useUpper = builder.useUpper;
    this.useDigits = builder.useDigits;
    this.usePunctuation = builder.usePunctuation;
  }

  public static class PasswordGeneratorBuilder {

    private boolean useLower;
    private boolean useUpper;
    private boolean useDigits;
    private boolean usePunctuation;

    public PasswordGeneratorBuilder() {
      this.useLower = false;
      this.useUpper = false;
      this.useDigits = false;
      this.usePunctuation = false;
    }

    public PasswordGeneratorBuilder useLower(boolean useLower) {
      this.useLower = useLower;
      return this;
    }

    public PasswordGeneratorBuilder useUpper(boolean useUpper) {
      this.useUpper = useUpper;
      return this;
    }

    public PasswordGeneratorBuilder useDigits(boolean useDigits) {
      this.useDigits = useDigits;
      return this;
    }

    public PasswordGeneratorBuilder usePunctuation(boolean usePunctuation) {
      this.usePunctuation = usePunctuation;
      return this;
    }

    public PasswordGenerator build() {
      return new PasswordGenerator(this);
    }
  }

  public String generate(int length) {
    // 비밀번호 길이가 주어질때만 생성하도록한다.
    if (length <= 0) return "";

    StringBuilder password = new StringBuilder(length);
    Random random = new Random(System.nanoTime());

    // 비밀번호 생성 범위 추가
    List<String> charCategories = new ArrayList<>(4);
    if (useLower) charCategories.add(LOWER);
    if (useUpper) charCategories.add(UPPER);
    if (useDigits) charCategories.add(DIGITS);
    if (usePunctuation) charCategories.add(PUNCTUATION);

    // 비밀번호 생성
    for (int i = 0; i < length; i++) {
      String charCategory = charCategories.get(random.nextInt(charCategories.size()));
      int position = random.nextInt(charCategory.length());
      password.append(charCategory.charAt(position));
    }
    return new String(password);
  }
}
