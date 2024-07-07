package utilz;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class LoadSave {

	public static BufferedImage LoadImage(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			img = ImageIO.read(is);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}
	
	public static ArrayList<int[][]> GetMapDataFromFile(String fileName) {
        ArrayList<int[][]> arrayList = new ArrayList<>();

        String filePath = "res/mapdata/" + fileName + ".txt";
        
        try {
            // Read the entire file content into a single string
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            bufferedReader.close();

            // Split the content by empty lines to get individual 2D arrays
            String[] parts = sb.toString().split("\\n\\s*\\n");
            
            for (String part : parts) {
                if (!part.trim().isEmpty()) {
                    // Split each part by lines to get rows
                    String[] lines = part.trim().split("\n");
                    List<int[]> rows = new ArrayList<>();
                    for (String row : lines) {
                        // Split each row by commas to get values
                        String[] values = row.split(",");
                        int[] intValues = new int[values.length];
                        for (int i = 0; i < values.length; i++) {
                            intValues[i] = Integer.parseInt(values[i].trim());
                        }
                        rows.add(intValues);
                    }
                    // Convert the list of rows into a 2D array and add it to the list
                    int[][] array = rows.toArray(new int[rows.size()][]);
                    arrayList.add(array);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayList;
    }
	
}
