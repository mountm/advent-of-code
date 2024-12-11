package Day12;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Day12Runner {
    public static void main(String[] args) {
        BufferedReader input = null;
        String currentLine;
        try {
            input = new BufferedReader(new FileReader("inputs/network.txt"));
            while ((currentLine = input.readLine()) != null) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
