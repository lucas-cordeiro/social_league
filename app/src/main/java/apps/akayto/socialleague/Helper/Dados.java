package apps.akayto.socialleague.Helper;

import android.content.Context;

/**
 * Created by LUCASGABRIELALVESCOR on 13/03/2018.
 */

public class Dados {
    private static boolean isChar(char text)
    {
        boolean texto = true;

        char a = 'a';
        boolean verifica_a = true;
        while (verifica_a)
        {
            if (a > 'z')
            {
                switch (text)
                {
                    case 'ã':
                        break;
                    case 'ç':
                        break;
                    case 'é':
                        break;
                    case 'ú':
                        break;
                    case 'í':
                        break;
                    default:
                        texto = false;
                        verifica_a = false;
                        break;
                }
            }
            if (a == text)
            {
                verifica_a = false;
            }
            else
            {
                a++;
            }
        }

        return texto;
    }

    private static boolean isChar2(char text)
    {
        boolean texto = true;

        char a = 'a';
        boolean verifica_a = true;
        while (verifica_a)
        {
            if (a > 'z')
            {
                switch (text)
                {
                    case '0':
                        break;
                    case '1':
                        break;
                    case '2':
                        break;
                    case '3':
                        break;
                    case '4':
                        break;
                    case '5':
                        break;
                    case '6':
                        break;
                    case '7':
                        break;
                    case '8':
                        break;
                    case '9':
                        break;
                    case '@':
                        break;
                    case '.':
                        break;

                    default:
                        texto = false;
                        verifica_a = false;
                        break;
                }
            }
            if (a == text)
            {
                verifica_a = false;
            }
            else
            {
                a++;
            }
        }

        return texto;
    }

    private static boolean isCharMinusculo(char character)
    {
        boolean caracter = true;

        char letra = character;
        char a = 'a';
        boolean verifica_A = true;

        while (verifica_A)
        {
            if (a > 'z')
            {
                caracter = false;
                verifica_A = false;
            }
            if (a == letra)
            {
                verifica_A = false;
            }
            else
            {
                a++;
            }
        }

        return caracter;
    }

    private static boolean isCharMaiusculo(char character)
    {
        boolean caracter = true;

        char A = 'A';
        boolean verifica_A = true;

        while (verifica_A)
        {
            if (A > 'Z')
            {
                caracter = false;
                verifica_A = false;
            }
            if (A == character)
            {
                verifica_A = false;
            }
            else
            {
                A++;
            }
        }

        return caracter;
    }

    public static boolean isString(String text) {
        boolean texto = true;

        if (!text.equals("")) {

            boolean vdd = true;
            while (vdd) {
                int tamanho = text.length();
                boolean vdd2 = true;
                int n = 0;
                while (vdd2) {
                    if (n >= tamanho) {
                        vdd2 = false;
                        vdd = false;
                    } else {
                        char letra = text.charAt(n);
                        char a = 'a';
                        boolean verifica_a = true;
                        while (verifica_a) {
                            if (a > 'z') {
                                switch (letra) {
                                    case 'ã':
                                        break;
                                    case 'ç':
                                        break;
                                    case 'é':
                                        break;
                                    case 'ú':
                                        break;
                                    case 'í':
                                        break;
                                    case ' ':
                                        break;
                                    case '\'':
                                        break;
                                    default:
                                        texto = false;
                                        verifica_a = false;
                                        vdd2 = false;
                                        vdd = false;

                                        break;
                                }
                            }
                            if (a == letra) {
                                verifica_a = false;
                            } else {
                                a++;
                            }
                        }

                        n++;
                    }
                }
            }
            return texto;
        }
        else
            return  false;

    }

    public static boolean leNome(String nome)
    {
        boolean vdd = true;
        boolean txt = false;

        while(vdd)
        {


            if(nome != null)
            {
                nome = nome.toLowerCase();

                if(isString(nome))
                {
                    int tamanho = nome.length();
                    int[] posicoes = new int[tamanho];
                    int[] posicoes2 = new int[tamanho];
                    for(int cont=0; cont<tamanho; cont++)
                    {
                        posicoes[cont]=0;
                    }

                    int letra=0;

                    for(int n=0; n<tamanho; n++)
                    {
                        if(isChar(nome.charAt(n)))
                        {
                            letra++;
                        }

                        else
                        {
                            int cont;

                            for(cont=0; cont<tamanho; cont++)
                            {
                                if(posicoes[cont]==0)
                                {
                                    posicoes[cont]=n+1;
                                    break;
                                }
                            }
                            if(letra>3)
                            {
                                letra = 0;
                            }

                            else
                            {
                                if(n-2>0 && (nome.charAt(n-1)=='a' || nome.charAt(n-1)=='e' || nome.charAt(n-1)=='i' || nome.charAt(n-1)=='o' || nome.charAt(n-1)=='u') && nome.charAt(n-2)==' ')
                                {
                                    for(cont=0; cont<tamanho; cont++)
                                    {
                                        if(posicoes2[cont]==0)
                                        {
                                            posicoes2[cont]=n+1;
                                            break;
                                        }
                                    }

                                    letra=0;
                                }
                                else if((letra==2||letra==3)&& n-3>=0)
                                {
                                    if(nome.charAt(n-2)=='d' || nome.charAt(n-3)=='d')
                                    {
                                        posicoes[cont-1]=0;

                                        letra = 0;
                                    }
                                    else
                                    {
                                        nome = "";
                                        break;
                                    }


                                }
                                else
                                {
                                    nome = "";
                                    break;
                                }
                            }
                        }
                    }

                }
                else
                {
                    nome = "N";
                }
            }

            if(!nome.equals("N") && !nome.equals(""))
                txt=true;

            vdd=false;
        }
        return txt;
    }

    public static String arrumaNome(String nome)
    {
        boolean vdd = true;

        while(vdd)
        {

            if(nome != null)
            {
                nome = nome.toLowerCase();

                if(isString(nome))
                {
                    int tamanho = nome.length();
                    int[] posicoes = new int[tamanho];
                    int[] posicoes2 = new int[tamanho];
                    for(int cont=0; cont<tamanho; cont++)
                    {
                        posicoes[cont]=0;
                    }

                    int letra=0;

                    for(int n=0; n<tamanho; n++)
                    {
                        if(isChar(nome.charAt(n)))
                        {
                            letra++;
                        }

                        else
                        {
                            int cont;

                            for(cont=0; cont<tamanho; cont++)
                            {
                                if(posicoes[cont]==0)
                                {
                                    posicoes[cont]=n+1;
                                    break;
                                }
                            }
                            if(letra>3)
                            {
                                letra = 0;
                            }

                            else
                            {
                                if(n-2>0 && (nome.charAt(n-1)=='a' || nome.charAt(n-1)=='e' || nome.charAt(n-1)=='i' || nome.charAt(n-1)=='o' || nome.charAt(n-1)=='u') && nome.charAt(n-2)==' ')
                                {
                                    for(cont=0; cont<tamanho; cont++)
                                    {
                                        if(posicoes2[cont]==0)
                                        {
                                            posicoes2[cont]=n+1;
                                            break;
                                        }
                                    }

                                    letra=0;
                                }
                                else if((letra==2||letra==3)&& n-3>=0)
                                {
                                    if(nome.charAt(n-2)=='d' || nome.charAt(n-3)=='d')
                                    {
                                        posicoes[cont-1]=0;

                                        letra = 0;
                                    }
                                    else
                                    {
                                        nome = "";
                                        break;
                                    }


                                }
                                else
                                {
                                    nome = "";
                                    break;
                                }
                            }
                        }
                    }

                    if(!nome.equals(""))
                    {
                        nome = nome.substring(0,1).toUpperCase() + nome.substring(1);
                        for(int cont=0; cont<tamanho; cont++)
                        {
                            if(posicoes[cont]!=0)
                            {
                                nome = nome.substring(0, posicoes[cont]) + nome.substring(posicoes[cont],posicoes[cont]+1).toUpperCase()+nome.substring(posicoes[cont]+1);
                            }
                            if(posicoes2[cont]!=0)
                            {
                                nome = nome.substring(0, posicoes[cont]) + nome.substring(posicoes[cont],posicoes[cont]+1).toLowerCase()+nome.substring(posicoes[cont]+1);
                            }
                        }
                    }
                }
                else
                {
                    nome = "N";
                }
            }

            if(!nome.equals("N") && !nome.equals(""))
                vdd=false;
        }
        return nome;
    }

    public static boolean verificaEmail(String email)
    {
        boolean mail = true;

        int tamanho = email.length();

        if (tamanho < 7)
        {
            mail = false;
        }
        else
        {
            for (int n = 0; n < tamanho; n++)
            {
                mail = isChar2(email.charAt(n));

                if (!mail)
                    break;
            }

            boolean vdd = false;

            for (int n = 0; n < tamanho; n++)
            {
                if (email.charAt(n) == '@')
                {
                    if (n + 1 < tamanho)
                    {
                        if (isChar(email.charAt(n + 1)))
                        {
                            if (!vdd)
                            {
                                vdd = true;
                            }
                            else
                            {
                                vdd = false;
                                break;
                            }
                        }
                    }

                }
            }

            mail = vdd;
        }

        if (mail)
        {
            mail = false;

            for (int n = 0; n < tamanho; n++)
            {
                if (email.charAt(n) == '.')
                {
                    if (n + 1 < tamanho && n + 2 < tamanho)
                    {
                        if (isChar(email.charAt(n+ 1)) && isChar(email.charAt(n + 2)))
                        {
                            mail = true;
                        }
                    }
                }
            }
        }

        return mail;

    }


    public static boolean verificaSenha(String texto)
    {
        boolean password = true;

        int tamanho = texto.length();

        if (tamanho < 8)
        {
            password = false;
        }

        else
        {
            boolean minuscula = false;

            boolean maiuscula = false;

            for (int n = 0; n < tamanho; n++)
            {
                if (isCharMaiusculo(texto.charAt(n)))
                {
                    maiuscula = true;
                }
                if (isCharMinusculo(texto.charAt(n)))
                {
                    minuscula = true;
                }

                if (maiuscula && minuscula)
                {
                    break;
                }
            }

            if (maiuscula && minuscula)
            {
                boolean numero = false;

                for (int n = 0; n < tamanho; n++)
                {
                    if (!numero)
                    {
                        try
                        {
                            byte num = (byte)texto.charAt(n);
                            numero = true;
                            break;
                        }
                        catch(NumberFormatException erro)
                        {

                        }
                    }
                }

                password = numero;
            }
            else
            {

                password = false;
            }
        }

        return password;
    }

    public static int dpToPx(int dp, Context context) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }


}
