package com.example.client.Encryption;

import com.example.client.LoggedException;

import java.util.Arrays;
import java.util.Random;


public class Mars implements Encryptor {
    private static final int S0_SIZE = 256;

    private static final int[] sBox = {
            0x09d0c479, 0x28c8ffe0, 0x84aa6c39, 0x9dad7287, 0x7dff9be3, 0xd4268361, 0xc96da1d4, 0x7974cc93, 0x85d0582e, 0x2a4b5705,
            0x1ca16a62, 0xc3bd279d, 0x0f1f25e5, 0x5160372f, 0xc695c1fb, 0x4d7ff1e4, 0xae5f6bf4, 0x0d72ee46, 0xff23de8a, 0xb1cf8e83,
            0xf14902e2, 0x3e981e42, 0x8bf53eb6, 0x7f4bf8ac, 0x83631f83, 0x25970205, 0x76afe784, 0x3a7931d4, 0x4f846450, 0x5c64c3f6,
            0x210a5f18, 0xc6986a26, 0x28f4e826, 0x3a60a81c, 0xd340a664, 0x7ea820c4, 0x526687c5, 0x7eddd12b, 0x32a11d1d, 0x9c9ef086,
            0x80f6e831, 0xab6f04ad, 0x56fb9b53, 0x8b2e095c, 0xb68556ae, 0xd2250b0d, 0x294a7721, 0xe21fb253, 0xae136749, 0xe82aae86,
            0x93365104, 0x99404a66, 0x78a784dc, 0xb69ba84b, 0x04046793, 0x23db5c1e, 0x46cae1d6, 0x2fe28134, 0x5a223942, 0x1863cd5b,
            0xc190c6e3, 0x07dfb846, 0x6eb88816, 0x2d0dcc4a, 0xa4ccae59, 0x3798670d, 0xcbfa9493, 0x4f481d45, 0xeafc8ca8, 0xdb1129d6,
            0xb0449e20, 0x0f5407fb, 0x6167d9a8, 0xd1f45763, 0x4daa96c3, 0x3bec5958, 0xababa014, 0xb6ccd201, 0x38d6279f, 0x02682215,
            0x8f376cd5, 0x092c237e, 0xbfc56593, 0x32889d2c, 0x854b3e95, 0x05bb9b43, 0x7dcd5dcd, 0xa02e926c, 0xfae527e5, 0x36a1c330,
            0x3412e1ae, 0xf257f462, 0x3c4f1d71, 0x30a2e809, 0x68e5f551, 0x9c61ba44, 0x5ded0ab8, 0x75ce09c8, 0x9654f93e, 0x698c0cca,
            0x243cb3e4, 0x2b062b97, 0x0f3b8d9e, 0x00e050df, 0xfc5d6166, 0xe35f9288, 0xc079550d, 0x0591aee8, 0x8e531e74, 0x75fe3578,
            0x2f6d829a, 0xf60b21ae, 0x95e8eb8d, 0x6699486b, 0x901d7d9b, 0xfd6d6e31, 0x1090acef, 0xe0670dd8, 0xdab2e692, 0xcd6d4365,
            0xe5393514, 0x3af345f0, 0x6241fc4d, 0x460da3a3, 0x7bcf3729, 0x8bf1d1e0, 0x14aac070, 0x1587ed55, 0x3afd7d3e, 0xd2f29e01,
            0x29a9d1f6, 0xefb10c53, 0xcf3b870f, 0xb414935c, 0x664465ed, 0x024acac7, 0x59a744c1, 0x1d2936a7, 0xdc580aa6, 0xcf574ca8,
            0x040a7a10, 0x6cd81807, 0x8a98be4c, 0xaccea063, 0xc33e92b5, 0xd1e0e03d, 0xb322517e, 0x2092bd13, 0x386b2c4a, 0x52e8dd58,
            0x58656dfb, 0x50820371, 0x41811896, 0xe337ef7e, 0xd39fb119, 0xc97f0df6, 0x68fea01b, 0xa150a6e5, 0x55258962, 0xeb6ff41b,
            0xd7c9cd7a, 0xa619cd9e, 0xbcf09576, 0x2672c073, 0xf003fb3c, 0x4ab7a50b, 0x1484126a, 0x487ba9b1, 0xa64fc9c6, 0xf6957d49,
            0x38b06a75, 0xdd805fcd, 0x63d094cf, 0xf51c999e, 0x1aa4d343, 0xb8495294, 0xce9f8e99, 0xbffcd770, 0xc7c275cc, 0x378453a7,
            0x7b21be33, 0x397f41bd, 0x4e94d131, 0x92cc1f98, 0x5915ea51, 0x99f861b7, 0xc9980a88, 0x1d74fd5f, 0xb0a495f8, 0x614deed0,
            0xb5778eea, 0x5941792d, 0xfa90c1f8, 0x33f824b4, 0xc4965372, 0x3ff6d550, 0x4ca5fec0, 0x8630e964, 0x5b3fbbd6, 0x7da26a48,
            0xb203231a, 0x04297514, 0x2d639306, 0x2eb13149, 0x16a45272, 0x532459a0, 0x8e5f4872, 0xf966c7d9, 0x07128dc0, 0x0d44db62,
            0xafc8d52d, 0x06316131, 0xd838e7ce, 0x1bc41d00, 0x3a2e8c0f, 0xea83837e, 0xb984737d, 0x13ba4891, 0xc4f8b949, 0xa6d6acb3,
            0xa215cdce, 0x8359838b, 0x6bd1aa31, 0xf579dd52, 0x21b93f93, 0xf5176781, 0x187dfdde, 0xe94aeb76, 0x2b38fd54, 0x431de1da,
            0xab394825, 0x9ad3048f, 0xdfea32aa, 0x659473e3, 0x623f7863, 0xf3346c59, 0xab3ab685, 0x3346a90b, 0x6b56443e, 0xc6de01f8,
            0x8d421fc0, 0x9b0ed10c, 0x88f1a1e9, 0x54c1f029, 0x7dead57b, 0x8d7ba426, 0x4cf5178a, 0x551a7cca, 0x1a9a5f08, 0xfcd651b9,
            0x25605182, 0xe11fc6c3, 0xb6fd9676, 0x337b3027, 0xb7c8eb14, 0x9e5fd030, 0x6b57e354, 0xad913cf7, 0x7e16688d, 0x58872a69,
            0x2c2fc7df, 0xe389ccc6, 0x30738df1, 0x0824a734, 0xe1797a8b, 0xa4a8d57b, 0x5b5d193b, 0xc8a8309b, 0x73f9a978, 0x73398d32,
            0x0f59573e, 0xe9df2b03, 0xe8a5b6c8, 0x848d0704, 0x98df93c2, 0x720a1dc3, 0x684f259a, 0x943ba848, 0xa6370152, 0x863b5ea3,
            0xd17b978b, 0x6d9b58ef, 0x0a700dd4, 0xa73d36bf, 0x8e6a0829, 0x8695bc14, 0xe35b3447, 0x933ac568, 0x8894b022, 0x2f511c27,
            0xddfbcc3c, 0x006662b6, 0x117c83fe, 0x4e12b414, 0xc2bca766, 0x3a2fec10, 0xf4562420, 0x55792e2a, 0x46f5d857, 0xceda25ce,
            0xc3601d3b, 0x6c00ab46, 0xefac9c28, 0xb3c35047, 0x611dfee3, 0x257c3207, 0xfdd58482, 0x3b14d84f, 0x23becb64, 0xa075f3a3,
            0x088f8ead, 0x07adf158, 0x7796943c, 0xfacabf3d, 0xc09730cd, 0xf7679969, 0xda44e9ed, 0x2c854c12, 0x35935fa3, 0x2f057d9f,
            0x690624f8, 0x1cb0bafd, 0x7b0dbdc6, 0x810f23bb, 0xfa929a1a, 0x6d969a17, 0x6742979b, 0x74ac7d05, 0x010e65c4, 0x86a3d963,
            0xf907b5a0, 0xd0042bd3, 0x158d7d03, 0x287a8255, 0xbba8366f, 0x096edc33, 0x21916a7b, 0x77b56b86, 0x951622f9, 0xa6c5e650,
            0x8cea17d1, 0xcd8c62bc, 0xa3d63433, 0x358a68fd, 0x0f9b9d3c, 0xd6aa295b, 0xfe33384a, 0xc000738e, 0xcd67eb2f, 0xe2eb6dc2,
            0x97338b02, 0x06c9f246, 0x419cf1ad, 0x2b83c045, 0x3723f18a, 0xcb5b3089, 0x160bead7, 0x5d494656, 0x35f8a74b, 0x1e4e6c9e,
            0x000399bd, 0x67466880, 0xb4174831, 0xacf423b2, 0xca815ab3, 0x5a6395e7, 0x302a67c5, 0x8bdb446b, 0x108f8fa4, 0x10223eda,
            0x92b8b48b, 0x7f38d0ee, 0xab2701d4, 0x0262d415, 0xaf224a30, 0xb3d88aba, 0xf8b2c3af, 0xdaf7ef70, 0xcc97d3b7, 0xe9614b6c,
            0x2baebff4, 0x70f687cf, 0x386c9156, 0xce092ee5, 0x01e87da6, 0x6ce91e6a, 0xbb7bcc84, 0xc7922c20, 0x9d3b71fd, 0x060e41c6,
            0xd7590f15, 0x4e03bb47, 0x183c198e, 0x63eeb240, 0x2ddbf49a, 0x6d5cba54, 0x923750af, 0xf9e14236, 0x7838162b, 0x59726c72,
            0x81b66760, 0xbb2926c1, 0x48a0ce0d, 0xa6c0496d, 0xad43507b, 0x718d496a, 0x9df057af, 0x44b1bde6, 0x054356dc, 0xde7ced35,
            0xd51a138b, 0x62088cc9, 0x35830311, 0xc96efca2, 0x686f86ec, 0x8e77cb68, 0x63e1d6b8, 0xc80f9778, 0x79c491fd, 0x1b4c67f2,
            0x58656dfb, 0x50820371, 0x41811896, 0xe337ef7e, 0xd39fb119, 0xc97f0df6, 0x68fea01b, 0xa150a6e5, 0x55258962, 0xeb6ff41b,
            0xd7c9cd7a, 0xa619cd9e, 0xbcf09576, 0x2672c073, 0xf003fb3c, 0x4ab7a50b, 0x1484126a, 0x487ba9b1, 0xa64fc9c6, 0xf6957d49,
            0x38b06a75, 0xdd805fcd, 0x63d094cf, 0xf51c999e, 0x1aa4d343, 0xb8495294, 0xce9f8e99, 0xbffcd770, 0xc7c275cc, 0x378453a7,
            0x7b21be33, 0x397f41bd, 0x4e94d131, 0x92cc1f98, 0x5915ea51, 0x99f861b7, 0xc9980a88, 0x1d74fd5f, 0xb0a495f8, 0x614deed0,
            0xb5778eea, 0x5941792d, 0xfa90c1f8, 0x33f824b4, 0xc4965372, 0x3ff6d550, 0x4ca5fec0, 0x8630e964, 0x5b3fbbd6, 0x7da26a48,
            0xb203231a, 0x04297514, 0x2d639306, 0x2eb13149, 0x16a45272, 0x532459a0, 0x8e5f4872, 0xf966c7d9, 0x07128dc0, 0x0d44db62,
            0xafc8d52d, 0x06316131, 0xd838e7ce, 0x1bc41d00, 0x3a2e8c0f, 0xea83837e, 0xb984737d, 0x13ba4891, 0xc4f8b949, 0xa6d6acb3,
            0xa215cdce, 0x8359838b, 0x6bd1aa31, 0xf579dd52, 0x21b93f93, 0xf5176781, 0x187dfdde, 0xe94aeb76, 0x2b38fd54, 0x431de1da,
            0xab394825, 0x9ad3048f, 0xdfea32aa, 0x659473e3, 0x623f7863, 0xf3346c59, 0xab3ab685, 0x3346a90b, 0x6b56443e, 0xc6de01f8,
            0x8d421fc0, 0x9b0ed10c, 0x88f1a1e9, 0x54c1f029, 0x7dead57b, 0x8d7ba426, 0x4cf5178a, 0x551a7cca, 0x1a9a5f08, 0xfcd651b9,
            0x25605182, 0xe11fc6c3, 0xb6fd9676, 0x337b3027, 0xb7c8eb14, 0x9e5fd030, 0x6b57e354, 0xad913cf7, 0x7e16688d, 0x58872a69,
            0x2c2fc7df, 0xe389ccc6, 0x30738df1, 0x0824a734, 0xe1797a8b, 0xa4a8d57b, 0x5b5d193b, 0xc8a8309b, 0x73f9a978, 0x73398d32,
            0x0f59573e, 0xe9df2b03, 0xe8a5b6c8, 0x848d0704, 0x98df93c2, 0x720a1dc3, 0x684f259a, 0x943ba848, 0xa6370152, 0x863b5ea3,
            0xd17b978b, 0x6d9b58ef, 0x0a700dd4, 0xa73d36bf, 0x8e6a0829, 0x8695bc14, 0xe35b3447, 0x933ac568, 0x8894b022, 0x2f511c27,
            0xddfbcc3c, 0x006662b6, 0x117c83fe, 0x4e12b414, 0xc2bca766, 0x3a2fec10, 0xf4562420, 0x55792e2a, 0x46f5d857, 0xceda25ce,
            0xc3601d3b, 0x6c00ab46, 0xefac9c28, 0xb3c35047, 0x611dfee3, 0x257c3207, 0xfdd58482, 0x3b14d84f, 0x23becb64, 0xa075f3a3,
            0x088f8ead, 0x07adf158, 0x7796943c, 0xfacabf3d, 0xc09730cd, 0xf7679969, 0xda44e9ed, 0x2c854c12, 0x35935fa3, 0x2f057d9f,
            0x690624f8, 0x1cb0bafd, 0x7b0dbdc6, 0x810f23bb, 0xfa929a1a, 0x6d969a17, 0x6742979b, 0x74ac7d05, 0x010e65c4, 0x86a3d963,
            0xf907b5a0, 0xd0042bd3, 0x158d7d03, 0x287a8255, 0xbba8366f, 0x096edc33, 0x21916a7b, 0x77b56b86, 0x951622f9, 0xa6c5e650,
            0x8cea17d1, 0xcd8c62bc, 0xa3d63433, 0x358a68fd, 0x0f9b9d3c, 0xd6aa295b, 0xfe33384a, 0xc000738e, 0xcd67eb2f, 0xe2eb6dc2,
            0x97338b02, 0x06c9f246, 0x419cf1ad, 0x2b83c045, 0x3723f18a, 0xcb5b3089, 0x160bead7, 0x5d494656, 0x35f8a74b, 0x1e4e6c9e,
            0x000399bd, 0x67466880, 0xb4174831, 0xacf423b2, 0xca815ab3, 0x5a6395e7, 0x302a67c5, 0x8bdb446b, 0x108f8fa4, 0x10223eda,
            0x92b8b48b, 0x7f38d0ee, 0xab2701d4, 0x0262d415, 0xaf224a30, 0xb3d88aba, 0xf8b2c3af, 0xdaf7ef70, 0xcc97d3b7, 0xe9614b6c,
            0x2baebff4, 0x70f687cf, 0x386c9156, 0xce092ee5, 0x01e87da6, 0x6ce91e6a, 0xbb7bcc84, 0xc7922c20, 0x9d3b71fd, 0x060e41c6,
            0xd7590f15, 0x4e03bb47, 0x183c198e, 0x63eeb240, 0x2ddbf49a, 0x6d5cba54, 0x923750af, 0xf9e14236, 0x7838162b, 0x59726c72,
            0x81b66760, 0xbb2926c1, 0x48a0ce0d, 0xa6c0496d, 0xad43507b, 0x718d496a, 0x9df057af, 0x44b1bde6, 0x054356dc, 0xde7ced35,
            0xd51a138b, 0x62088cc9, 0x35830311, 0xc96efca2, 0x686f86ec, 0x8e77cb68, 0x63e1d6b8, 0xc80f9778, 0x79c491fd, 0x1b4c67f2,
    };

    protected int[] expandedKey = null;
    public int paddingSize;

    @Override
    public byte[] encrypt(byte[] in) {
        var blocks = splitInput(in);
        var result = new byte[blocks.length*16];
        for(int i =0; i<blocks.length;++i){
            var encrypted = encryptBlock(blocks[i]);
            System.arraycopy(encrypted,0,result,i*16,16);
        }
        return result;

    }

    @Override
    public byte[] decrypt(byte[] in) throws LoggedException {
        if (expandedKey == null) {
            throw new LoggedException("out of key!");
        }
        var totalSize = in.length;
        byte[] resultBlock = new byte[totalSize];
        byte[] tempBuf = new byte[16];
        int i = 0;
        for (i = 0; i < totalSize; i++) {
            if (i > 0 && i % 16 == 0) {
                tempBuf = decryptBlock(tempBuf);
                System.arraycopy(tempBuf, 0, resultBlock, i - 16, tempBuf.length);
            }
            if (i < totalSize) {
                tempBuf[i % 16] = in[i];
            }
        }
        tempBuf = decryptBlock(tempBuf);
        System.arraycopy(tempBuf, 0, resultBlock, i - 16, tempBuf.length);
        resultBlock = deletePadding(resultBlock);
        return resultBlock;
    }

    protected byte[] deletePadding(byte[] input) {
        int count = 0;
        int i = input.length - 1;
        while (input[i] == 0) {
            count++;
            i--;
            if(count == 16){
                return input;
            }
        }
        byte[] tmp;
        if (count == 0) {
            tmp = new byte[input.length];
        } else {
            tmp = new byte[input.length - count - 1];
        }
        System.arraycopy(input, 0, tmp, 0, tmp.length);
        return tmp;
    }


    /***
     * @param data - prepared 16-byte block
     * @return decrypted block
     */
    protected byte[] decryptBlock(byte[] data) {
        var dataSize = data.length;
        var words = keyOverlayD(data);
        forwardMixingD(words);
        cryptoCoreD(words);
        backwardMixingD(words);

        var res = new byte[dataSize];
        for (int i = 0; i < res.length; i++) {
            res[i] = (byte) ((words[i / 4] >>> (i % 4) * 8) & 0xff);
        }

        return res;
    }

    protected void backwardMixingD(int[] words) {
        var a = words[0];
        var b = words[1];
        var c = words[2];
        var d = words[3];
        for (int i = 7; i >= 0; --i) {
            var aux = d;
            d = c;
            c = b;
            b = a;
            a = aux;
            if (i == 0 || i == 4) {
                a = a - d;
            }
            if (i == 1 || i == 5) {
                a = a - b;
            }
            a = cycleShift("left", a, 24);
            d = d ^ sBox[S0_SIZE + (cycleShift("right", a, 24) & 0xff)];
            c = c - sBox[cycleShift("right", a, 16) & 0xff];
            b = b - sBox[S0_SIZE + (cycleShift("right", a, 8) & 0xff)];
            b = b ^ sBox[a & 0xff];
        }
        a = a - expandedKey[0];
        b = b - expandedKey[1];
        c = c - expandedKey[2];
        d = d - expandedKey[3];

        words[0] = a;
        words[1] = b;
        words[2] = c;
        words[3] = d;
    }

    protected void cryptoCoreD(int[] words) {
        var a = words[0];
        var b = words[1];
        var c = words[2];
        var d = words[3];
        for (int i = 15; i >= 0; --i) {
            var aux = d;
            d = c;
            c = b;
            b = a;
            a = aux;
            a = cycleShift("right", a, 13);
            var outs = eFunc(a, expandedKey[2 * i + 4], expandedKey[2 * i + 5]);
            c = c - outs[1];
            if (i < 8) {
                b = b - outs[0];
                d = d ^ outs[2];
            } else {
                d = d - outs[0];
                b = b ^ outs[2];
            }
        }
        words[0] = a;
        words[1] = b;
        words[2] = c;
        words[3] = d;
    }

    protected void forwardMixingD(int[] words) {
        var a = words[0];
        var b = words[1];
        var c = words[2];
        var d = words[3];
        for (int i = 7; i >= 0; i--) {
            var tmp = d;
            d = c;
            c = b;
            b = a;
            a = tmp;
            a = cycleShift("right", a, 24);
            d = d ^ sBox[cycleShift("right", a, 8) & 0xff];
            d = d + sBox[S0_SIZE + (cycleShift("right", a, 16) & 0xff)];
            c = c + sBox[cycleShift("right", a, 24) & 0xff];
            b = b ^ sBox[(a & 0xff) + 256];
            if (i == 2 || i == 6) {
                a = a + d;
            }
            if (i == 3 || i == 7) {
                a = a + b;
            }
        }
        words[0] = a;
        words[1] = b;
        words[2] = c;
        words[3] = d;
    }

    private int[] keyOverlayD(byte[] block) {
        var result = new int[4];
        for (int i = 0, j = 0; i < 4; ++i, j += 4) {
            var subBlock = BitManipulations.bytesToInt(Arrays.copyOfRange(block, j, j + 4));
            var subKey = expandedKey[36 + i];/*Arrays.copyOfRange(key, j, j + 4)*/
            var tempResult = subBlock + subKey;
            result[i] = tempResult;
        }
        return result;
    }


    public byte[][] splitInput(byte[] in) {
        byte[][] result;
        var totalSize = in.length;
        var blocksAmount = totalSize / 16;
        paddingSize = 0;
        var padding = new byte[0];
        int i = 0;
        int blockIndex = 0;
        int paddingIndex = 0;
        if (totalSize % 16 != 0) {
            result = new byte[blocksAmount + 1][16];
            paddingSize = 16 - totalSize % 16;
            padding = new byte[paddingSize];
//            padding[0] = (byte) 0x80;
//            for (i = 1; i < paddingSize; i++)
//                padding[i] = 0;
        } else {
            result = new byte[blocksAmount][16];
        }
        var tempBuf = new byte[16];
//        var resultBlock = new byte[totalSize + paddingSize];
        for (i = 0; i < totalSize + paddingSize; ++i) {
            if (i != 0 && i % 16 == 0) {
//                tempBuf = encryptBlock(tempBuf);
                System.arraycopy(tempBuf, 0, result[blockIndex]/*resultBlock*/, 0, tempBuf.length);
                ++blockIndex;
            }
            if (i < totalSize) {
                tempBuf[i % 16] = in[i];
            } else {
                tempBuf[i % 16] = padding[paddingIndex % 16];
                paddingIndex++;

            }
        }
        if (tempBuf.length == 16) {
//            tempBuf = encryptBlock(tempBuf);
            System.arraycopy(tempBuf, 0, result[blockIndex]/*resultBlock*/, 0, tempBuf.length);
        }
        return result;
    }

    /***
     *
     * @param data - prepared 16-byte block (w padding)
     * @return encrypted block
     */
    protected byte[] encryptBlock(byte[] data/*, byte[] key*/) {
        var dataSize = data.length;
        int[] words = null;
        words = keyOverlay(data);
        forwardMixing(words);
        cryptoCore(words);
        backwardMixing(words);

        var res = new byte[dataSize];
        for (int i = 0; i < res.length; i++) {
            res[i] = (byte) ((words[i / 4] >>> (i % 4) * 8) & 0xff);
        }
        return res;
    }

    /**
     * @param block - 16 byte block
     * @return data words with overlayed keyparts
     */
    protected int[] keyOverlay(byte[] block /*byte[] key*/) {
        var result = new int[4];
        for (int i = 0, j = 0; i < 4; ++i, j += 4) {
            var subBlock = BitManipulations.bytesToInt(Arrays.copyOfRange(block, j, j + 4));
            var subKey = expandedKey[i];/*Arrays.copyOfRange(key, j, j + 4)*/
            var tempResult = subBlock + subKey;
            result[i] = tempResult;
        }
        return result;
    }

    /**
     * @param dataWords - overlayed words
     * @return
     */
    protected void forwardMixing(int[] dataWords) {
        var a = dataWords[0];
        var b = dataWords[1];
        var c = dataWords[2];
        var d = dataWords[3];
        for (int i = 0; i < 8; ++i) {
            b = b ^ sBox[a & 0xff];
            b = b + sBox[S0_SIZE + (cycleShift("right", a, 8) & 0xff)];
            c = c + sBox[cycleShift("right", a, 16) & 0xff];
            d = d ^ sBox[S0_SIZE + (cycleShift("right", a, 24) & 0xff)];

            a = cycleShift("right", a, 24);

            if (i == 0 || i == 4) {
                a = a + d;
            }
            if (i == 1 || i == 5) {
                a = a + b;
            }

            var tmp = a;
            a = b;
            b = c;
            c = d;
            d = tmp;
        }
        dataWords[0] = a;
        dataWords[1] = b;
        dataWords[2] = c;
        dataWords[3] = d;
    }

    /**
     * @param dataWords - 4 32bit-size words
     */
    private void cryptoCore(int[] dataWords) {
        var a = dataWords[0];
        var b = dataWords[1];
        var c = dataWords[2];
        var d = dataWords[3];
        for (int i = 0; i < 16; ++i) {
            var outs = eFunc(a, expandedKey[2 * i + 4], expandedKey[2 * i + 5]);
            a = cycleShift("left", a, 13);
            c = c + outs[1];
            if (i < 8) {
                b = b + outs[0];
                d = d ^ outs[2];
            } else {
                d = d + outs[0];
                b = b ^ outs[2];
            }

            var tmp = a;
            a = b;
            b = c;
            c = d;
            d = tmp;
        }
        dataWords[0] = a;
        dataWords[1] = b;
        dataWords[2] = c;
        dataWords[3] = d;
    }

    /***
     * @param inWord
     * @param k1 - addBlock from expandedKey
     * @param k2 - addBlock from expandedKey
     * @return 3 int elements (outs)
     */
    protected int[] eFunc(int inWord, int k1, int k2) {
        int m, l, r, i;
        m = inWord + k1;
        r = cycleShift("left", inWord, 13) * k2;
        i = m & 0x000001ff; // lowest 9 bits of m
        l = sBox[i];
        r = cycleShift("left", r, 5);
        var rTemp = r & 0x0000001f;
        m = cycleShift("left", m, rTemp);
        l = l ^ r;
        r = cycleShift("left", r, 5);
        l = l ^ r;
        rTemp = r & 0x0000001f;
        l = cycleShift("left", l, rTemp);
        return new int[]{l, m, r};
    }

    protected void backwardMixing(int[] dataWords) {
        var a = dataWords[0];
        var b = dataWords[1];
        var c = dataWords[2];
        var d = dataWords[3];
        for (int i = 0; i < 8; i++) {
            if (i == 3 || i == 7) a = a - b;
            if (i == 2 || i == 6) a = a - d;

            b = b ^ sBox[(a & 0xff) + 256];
            c = c - sBox[cycleShift("right", a, 24) & 0xff];
            d = d - sBox[(cycleShift("right", a, 16) & 0xff) + 256];
            d = d ^ sBox[cycleShift("right", a, 8) & 0xff];
            a = cycleShift("left", a, 24);

            var aux = a;
            a = b;
            b = c;
            c = d;
            d = aux;
        }
        a = a - expandedKey[36];
        b = b - expandedKey[37];
        c = c - expandedKey[38];
        d = d - expandedKey[39];

        dataWords[0] = a;
        dataWords[1] = b;
        dataWords[2] = c;
        dataWords[3] = d;
    }

    protected static int cycleShift(String direction, int val, int shift) {
        if (direction.equals("left")) {
            return (val << shift) | (val >>> (32 - shift));
        } else {
            return (val >>> shift) | (val << (32 - shift));
        }
    }

    /***
     *
     * @param key
     */
    public void setKey(byte[] key) {
        expandedKey = expandKey(key);
    }

    @Override
    public byte[] getInitVector() {
        return new byte[0];
    }

    @Override
    public void setInitVector(byte[] block) {
        return;
    }


    protected int[] expandKey(byte[] key) {
        int n = key.length / 4;
        int[] tmp = new int[40];
        int[] data = new int[n];

        // compressing byte[] to int[]
        int off = 0;
        for (int i = 0; i < data.length; i++) {
            data[i] = ((key[off++] & 0xff)) |
                    ((key[off++] & 0xff) << 8) |
                    ((key[off++] & 0xff) << 16) |
                    ((key[off++] & 0xff) << 24);
        }
        int[] T = new int[15];
        // filling T
        for (int i = 0; i < T.length; i++) {
            if (i < data.length) T[i] = data[i];
            else if (i == data.length) T[i] = n;
            else T[i] = 0;
        }
        // transform T
        for (int j = 0; j < 4; ++j) {
            for (int i = 0; i < T.length; ++i) {
                T[i] = T[i] ^ (cycleShift("left", T[Math.abs(i - 7 % 15)] ^ T[Math.abs(i - 2 % 15)], 3) ^ (4 * i + j));
            }
            for (int c = 0; c < 4; ++c) {
                for (int i = 0; i < T.length; ++i) {
//                    T[i] = T[i] + cycleShift("left", sBox[(int)(T[Math.abs(i - 1 % 15)] & 0x000001ff)], 9);
                    var val = T[Math.abs(i - 1 % 15)] & 0x000001ff;
                    var val3 = T[Math.abs(i - 1 % 15)];
                    var val4 = (T[Math.abs(i - 1 % 15)] & 0x000001ff);
                    var val5 = (int) (T[Math.abs(i - 1 % 15)] & 0x000001ff);
                    var val6 = sBox[(int) (T[Math.abs(i - 1 % 15)] & 0x000001ff)];
                    var val2 = cycleShift("left", sBox[(int) (T[Math.abs(i - 1 % 15)] & 0x000001ff)], 9);
                    T[i] = T[i] + cycleShift("left", sBox[(int) (T[Math.abs(i - 1 % 15)] & 0x000001ff)], 9);
                }
            }
            for (int i = 0; i < 10; ++i) {
                tmp[10 * j + i] = T[4 * i % 15];
            }
        }

        // B elements and its cycle shifts has no 7 order elements w 1 or 0
        int[] B = {0xa4a8d57b, 0x5b5d193b, 0xc8a8309b, 0x73f9a978};
        int j, w, m, r, p;
        for (int i = 5; i <= 35; i++) {
            j = tmp[i] & 0x00000003;
            w = tmp[i] | 0x00000003;
            m = generateMask(w);
            r = tmp[i - 1] & 0x0000001f;//lowest 5 bits
            p = cycleShift("left", B[j], r);
            tmp[i] = w ^ (p & m);
        }

        return tmp;
    }

    protected int generateMask(int x) {
        int m;

        m = (~x ^ (x >>> 1)) & 0x7fffffff;
        m &= (m >> 1) & (m >> 2);
        m &= (m >> 3) & (m >> 6);

        if (m == 0)
            return 0;

        m <<= 1;
        m |= (m << 1);
        m |= (m << 2);
        m |= (m << 4);

        m |= (m << 1) & ~x & 0x80000000;

        return m & 0xfffffffc;
    }


    static public byte[] generatePublicKey(int size) {
        var result = new byte[size];
        for (int i = 0; i < size; ++i) {
            byte choice = (byte) (Math.random() * (3 - 0));
            byte val = '0';
            switch (choice) {
                case 0:
                    val = (byte) (Math.random() * (58 - 48) + 48);
                    break;
                case 1:
                    val = (byte) (Math.random() * (91 - 65) + 65);
                    break;
                case 2:
                    val = (byte) (Math.random() * (123 - 97) + 97);
                    break;
            }
            result[i] = val;
        }
        return result;
    }

    /***
     *
     * @return 16 random bytes
     */
    protected byte[] generateIV() {
        var random = new Random();
        byte[] res = new byte[16];
        for (int i = 0; i < 16; ++i) {
            var val = random.nextInt(256);
            res[i] = (byte) val;
        }
        return res;
    }
}

