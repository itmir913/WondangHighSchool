package wondang.icehs.kr.whdghks913.wondanghighschool.tool;

import android.util.Log;

import java.math.BigDecimal;

import itmir.tistory.com.xor.SecurityXor;

/**
 * Created by whdghks913 on 2015-12-20.
 */
public class HiddenCode {
    /**
     * PrivateCode.java 파일은 공개하지 않으며 단순히 int형 숫자를 선언해둔 파일이고,
     * 원당고 앱의 공지사항등을 임의로 게시할 수 없도록 설계되었습니다.
     * 빌드시 PrivateCode.java파일을 찾을 수 없다는 오류가 발생됩니다.
     * PrivateCode.MY_HIDDEN_FIRST_CODE, PrivateCode.MY_HIDDEN_SECOND_CODE을 임의의 상수로 바꾸세요.
     */
    public static final int HIDDEN_FIRST_CODE = PrivateCode.MY_HIDDEN_FIRST_CODE;
    public static final int HIDDEN_SECOND_CODE = PrivateCode.MY_HIDDEN_SECOND_CODE;

    public static String getHiddenCode() {
        BigDecimal bigDecimal = new BigDecimal(getFirstHiddenCode())
                .multiply(new BigDecimal(getSecondHiddenCode()));
        return bigDecimal.toString();
    }

    private static int getFirstHiddenCode() {
        // 7777
        SecurityXor securityXor = new SecurityXor();
        int num = securityXor.getSecurityXor(securityXor.getSecurityXor(258, HIDDEN_FIRST_CODE), 473);
        return num;
    }

    private static int getSecondHiddenCode() {
        // 1998913
        SecurityXor securityXor = new SecurityXor();
        int num = securityXor.getSecurityXor(securityXor.getSecurityXor(739, HIDDEN_SECOND_CODE), 238);
        return num;
    }
}
