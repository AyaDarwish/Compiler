package compilers_scanner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;
import static java.lang.Character.isWhitespace;
import java.util.HashMap;
import java.util.Map;


public class Compilers_Scanner 
{  
    public static void main(String[] args) throws Exception
    {
        // Reserved Words
        String [] reserved_words = {"if","then","else","end","repeat","until","read","write"};
        
        // Special Symbols
        Map <Character,String> special_symbol = new HashMap <>();
        special_symbol.put('+',"plus sign");
        special_symbol.put('-',"minus sign");
        special_symbol.put('*',"multiply");
        special_symbol.put('/',"divide");
        special_symbol.put('=',"equal");
        special_symbol.put('<',"less than");
        special_symbol.put('(',"left bracket");
        special_symbol.put(')',"right bracket");
        special_symbol.put(';',"semi colon");
        special_symbol.put(':',"assignment operator");
        
        
        // input file
        BufferedReader br = new BufferedReader(new FileReader("src_code.txt")); 

        // output file        
        BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));
       
        String line = br.readLine();
        while (line !=null)
        {   
            // 0 => Start State 
            int state = 0 , i = 0;
            String word = "",num = ""; 
            boolean isIdentifier = false ,justComment = false;          
            
            while(i<line.length())
            {
                char c = line.charAt(i);
                if(i == 0)
                    while(isWhitespace(line.charAt(i)))
                        i++;                      

                if (isLetter(c))
                {
                    word += c;
                    state = 1; // in case of word
                     if(i<line.length())
                        i++;
                }

                else if (isDigit(c))
                {
                    num +=c;
                    state = 2; // in case of num
                    if(i<line.length())
                        i++;
                }

                else if (c == '{')
                {
                    if (i==0)
                        justComment = true;
                        
                    while (c != '}')
                    {
                        i++;                      
                        if (i == line.length())
                        {
                            i = 0;
                            line = br.readLine();
                            
                            while (line.length() == 0)
                                line = br.readLine();                                                       
                        }                        
                        
                        c = line.charAt(i);                           
                    }
                    i++;
                     if (i < line.length())
                        justComment = false;
                }
                
                if (special_symbol.containsKey(c) || isWhitespace(c) || i+1 > line.length())
                {
                    switch(state)
                    {
                        case 1:
                            isIdentifier = true;
                            for (String s : reserved_words)
                            {
                                if (word.equals(s))
                                {                                 
                                    bw.write(s + " , reserved word\r\n");
                                    bw.flush();
                                    isIdentifier = false;                                   
                                    break;
                                }                                                                                      
                            }
                            
                            if (isIdentifier)
                            {
                                bw.write(word + " , identifier\r\n");
                                bw.flush();
                                isIdentifier = false;
                            }
                            word = "";
                            state = 0;

                        break;
                            
                        case 2:
                            bw.write(num + " , number\r\n");
                            bw.flush();
                            num ="";
                            state = 0;
                        break;
                    }
                    
                    if (special_symbol.containsKey(c) || isWhitespace(c))
                    {
                        if(isWhitespace(c))
                        while(i<line.length() && isWhitespace(line.charAt(i)))
                            i++;
                                               
                        else
                        {
                            if (c == ':')
                            {
                                bw.write(c + "= , "+special_symbol.get(c) + "\r\n");
                                bw.flush();
                                i++;
                                if (i<line.length())
                                    i++;
                            }
                            
                            else
                            {
                                bw.write(c + " , "+special_symbol.get(c) + "\r\n");
                                bw.flush();
                                if(i<line.length())
                                    i++;
                            }
                            
                        }

                    }                      
                }                               
            }
            if(!justComment)
            {
                bw.write ("\r\n");
                bw.flush();
            }
            line = br.readLine();
            
        }              
    }
}
    