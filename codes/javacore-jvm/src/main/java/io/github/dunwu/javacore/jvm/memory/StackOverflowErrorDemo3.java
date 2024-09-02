package io.github.dunwu.javacore.jvm.memory;

/**
 * 虚拟机栈和本地方法栈测试
 */
public class StackOverflowErrorDemo3 {

    private static int stackLength = 0;

    public static void test() {
        long unused1, unused2, unused3, unused4, unused5,
            unused6, unused7, unused8, unused9, unused10,
            unused11, unused12, unused13, unused14, unused15,
            unused16, unused17, unused18, unused19, unused20,
            unused21, unused22, unused23, unused24, unused25,
            unused26, unused27, unused28, unused29, unused30,
            unused31, unused32, unused33, unused34, unused35,
            unused36, unused37, unused38, unused39, unused40,
            unused41, unused42, unused43, unused44, unused45,
            unused46, unused47, unused48, unused49, unused50,
            unused51, unused52, unused53, unused54, unused55,
            unused56, unused57, unused58, unused59, unused60,
            unused61, unused62, unused63, unused64, unused65,
            unused66, unused67, unused68, unused69, unused70,
            unused71, unused72, unused73, unused74, unused75,
            unused76, unused77, unused78, unused79, unused80,
            unused81, unused82, unused83, unused84, unused85,
            unused86, unused87, unused88, unused89, unused90,
            unused91, unused92, unused93, unused94, unused95,
            unused96, unused97, unused98, unused99, unused100;

        stackLength++;
        test();

        unused1 = unused2 = unused3 = unused4 = unused5 =
            unused6 = unused7 = unused8 = unused9 = unused10 =
                unused11 = unused12 = unused13 = unused14 = unused15 =
                    unused16 = unused17 = unused18 = unused19 = unused20 =
                        unused21 = unused22 =
                            unused23 = unused24 = unused25 = unused26 = unused27 = unused28 = unused29 = unused30 =
                                unused31 = unused32 = unused33 = unused34 = unused35 =
                                    unused36 = unused37 = unused38 = unused39 = unused40 =
                                        unused41 = unused42 = unused43 = unused44 = unused45 =
                                            unused46 = unused47 = unused48 = unused49 = unused50 =
                                                unused51 = unused52 = unused53 = unused54 = unused55 =
                                                    unused56 = unused57 = unused58 = unused59 = unused60 =
                                                        unused61 = unused62 = unused63 = unused64 = unused65 =
                                                            unused66 = unused67 = unused68 = unused69 = unused70 =
                                                                unused71 = unused72 = unused73 = unused74 = unused75 =
                                                                    unused76 =
                                                                        unused77 = unused78 = unused79 = unused80 =
                                                                            unused81 = unused82 =
                                                                                unused83 = unused84 = unused85 =
                                                                                    unused86 = unused87 =
                                                                                        unused88 = unused89 = unused90 =
                                                                                            unused91 = unused92 =
                                                                                                unused93 = unused94 =
                                                                                                    unused95 =
                                                                                                        unused96 =
                                                                                                            unused97 =
                                                                                                                unused98 =
                                                                                                                    unused99 =
                                                                                                                        unused100 =
                                                                                                                            0;
    }

    public static void main(String[] args) {
        try {
            test();
        } catch (Error e) {
            System.out.println("stack length:" + stackLength);
            throw e;
        }
    }

}
