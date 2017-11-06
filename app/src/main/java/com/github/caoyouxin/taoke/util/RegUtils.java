package com.github.caoyouxin.taoke.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jasontsang on 11/1/17.
 */
public class RegUtils {
    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isBankCard(String cardNo) {
        Pattern p = Pattern.compile("^\\d{16,19}$|^\\d{6}[- ]\\d{10,13}$|^\\d{4}[- ]\\d{4}[- ]\\d{4}[- ]\\d{4,7}$");
        Matcher m = p.matcher(cardNo);
        return m.matches();
    }

    public static boolean validateIdCard(String idCard) {
        String regIdCard = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";
        Pattern p = Pattern.compile(regIdCard);
        return p.matcher(idCard).matches();
    }

    public static boolean isMobileSimple(String string) {
        return isMatch("^[1]\\d{10}$", string);
    }

    public static boolean isMobileExact(String string) {
        return isMatch("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$", string);
    }

    public static boolean isTel(String string) {
        return isMatch("^0\\d{2,3}[- ]?\\d{7,8}", string);
    }

    public static boolean isIDCard15(String string) {
        return isMatch("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$", string);
    }

    public static boolean isIDCard18(String string) {
        return isMatch("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$", string);
    }

    public static boolean isIDCard(String string) {
        return isMatch("(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|x|X)$)", string);
    }

    public static String IDCardValidate(String IDStr) throws ParseException {
        String errorInfo = "";
        String[] ValCodeArr = new String[]{"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = new String[]{"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        } else {
            if (IDStr.length() == 18) {
                Ai = IDStr.substring(0, 17);
            } else if (IDStr.length() == 15) {
                Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
            }

            if (!isNumeric(Ai)) {
                errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
                return errorInfo;
            } else {
                String strYear = Ai.substring(6, 10);
                String strMonth = Ai.substring(10, 12);
                String strDay = Ai.substring(12, 14);
                if (!isDate(strYear + "-" + strMonth + "-" + strDay)) {
                    errorInfo = "身份证生日无效。";
                    return errorInfo;
                } else {
                    GregorianCalendar gc = new GregorianCalendar();
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        if (gc.get(Calendar.YEAR) - Integer.parseInt(strYear) > 150 || gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime() < 0L) {
                            errorInfo = "身份证生日不在有效范围。";
                            return errorInfo;
                        }
                    } catch (NumberFormatException var14) {
                        var14.printStackTrace();
                    } catch (ParseException var15) {
                        var15.printStackTrace();
                    }

                    if (Integer.parseInt(strMonth) <= 12 && Integer.parseInt(strMonth) != 0) {
                        if (Integer.parseInt(strDay) <= 31 && Integer.parseInt(strDay) != 0) {
                            Hashtable h = GetAreaCode();
                            if (h.get(Ai.substring(0, 2)) == null) {
                                errorInfo = "身份证地区编码错误。";
                                return errorInfo;
                            } else {
                                int TotalmulAiWi = 0;

                                int modValue;
                                for (modValue = 0; modValue < 17; ++modValue) {
                                    TotalmulAiWi += Integer.parseInt(String.valueOf(Ai.charAt(modValue))) * Integer.parseInt(Wi[modValue]);
                                }

                                modValue = TotalmulAiWi % 11;
                                String strVerifyCode = ValCodeArr[modValue];
                                Ai = Ai + strVerifyCode;
                                if (IDStr.length() == 18) {
                                    if (!Ai.equals(IDStr)) {
                                        errorInfo = "身份证无效，不是合法的身份证号码";
                                        return errorInfo;
                                    } else {
                                        return "有效";
                                    }
                                } else {
                                    return "有效";
                                }
                            }
                        } else {
                            errorInfo = "身份证日期无效";
                            return errorInfo;
                        }
                    } else {
                        errorInfo = "身份证月份无效";
                        return errorInfo;
                    }
                }
            }
        }
    }

    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static boolean isEmail(String string) {
        return isMatch("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", string);
    }

    public static boolean isURL(String string) {
        return isMatch("http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?", string);
    }

    public static boolean isChz(String string) {
        return isMatch("^[\\u4e00-\\u9fa5]+$", string);
    }

    public static boolean isUsername(String string) {
        return isMatch("^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$", string);
    }

    public static boolean isDate(String string) {
        return isMatch("^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$", string);
    }

    public static boolean isIP(String string) {
        return isMatch("((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)", string);
    }

    public static boolean isMatch(String regex, String string) {
        return !TextUtils.isEmpty(string) && Pattern.matches(regex, string);
    }

    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    public static boolean checkDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex, digit);
    }

    public static boolean checkDecimals(String decimals) {
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";
        return Pattern.matches(regex, decimals);
    }

    public static boolean checkBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex, blankSpace);
    }

    public static boolean checkBirthday(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, birthday);
    }

    public static boolean checkPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }
}
