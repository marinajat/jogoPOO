package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[50]; // quantidade de imagens para o mapa
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/res/maps/mapa02.txt"); // passando o caminho do mapa como parâmetro
    }

    public void getTileImage() {
        // usar um tile branco nos arrays 0 até 9.
        setup(0,"grass00", false);
        setup(1,"grass00", false);
        setup(2,"grass00", false);
        setup(3,"grass00", false);
        setup(4,"grass00", false);
        setup(5,"grass00", false);
        setup(6,"grass00", false);
        setup(7,"grass00", false);
        setup(8,"grass00", false);
        setup(9,"grass00", false);


        //Iniciando aqui o uso dos tiles porque é melhor para desenhar o mapa com dois digitos
        //grass
        setup(10,"grass00", false);
        setup(11,"grass01", false);

        //WATER
        setup(12,"water00", true);
        setup(13,"water01", true);
        setup(14,"water02", true);
        setup(15,"water03", true);
        setup(16,"water04", true);
        setup(17,"water05", true);
        setup(18,"water06", true);
        setup(19,"water07", true);
        setup(20,"water08", true);
        setup(21,"water09", true);
        setup(22,"water10", true);
        setup(23,"water11", true);
        setup(24,"water12", true);
        setup(25,"water13", true);

        //Tiles para road
        setup(26,"road00", false);
        setup(27,"road01", false);
        setup(28,"road02", false);
        setup(29,"road03", false);
        setup(30,"road04", false);
        setup(31,"road05", false);
        setup(32,"road06", false);
        setup(33,"road07", false);
        setup(34,"road08", false);
        setup(35,"road09", false);
        setup(36,"road10", false);
        setup(37,"road11", false);
        setup(38,"road12", false);

        //demais tiles
        //tiles para table
        setup(39,"table01", false);
        //tiles para wall
        setup(40,"wall", true);
        //tiles para tree
        setup(41,"tree", true);
        //tiles para hut
        setup(42,"hut", true);
        //tiles para Earth
        setup(43,"earth", false);
        //tiles para Earth
        setup(44,"floor01", false);
    }

    public void setup(int index, String imageName, boolean collision) {
        // forma mais prática de instanciar os tiles do jogo, reduzindo linhas e aumentando a performance
        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (row < gp.maxWorldRow) {
                String line = br.readLine();
                if (line == null) {
                    throw new IOException("Linha do mapa está nula, verifique o formato do arquivo: " + filePath);
                }

                String[] numbers = line.split(" ");
                if (numbers.length != gp.maxWorldCol) {
                    throw new IOException("Número de colunas na linha do mapa é diferente de maxWorldCol: " + filePath);
                }

                for (col = 0; col < gp.maxWorldCol; col++) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                }

                row++;
            }

            br.close();
            System.out.println("Mapa carregado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar o mapa: " + filePath);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Erro ao converter número no mapa: " + filePath);
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];

            // Certificar-se de que o tileNum é válido antes de desenhar
            if (tileNum >= 0 && tileNum < tile.length && tile[tileNum] != null) {
                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                        worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                        worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                        worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                    g2.drawImage(tile[tileNum].image, screenX, screenY, null);
                }
            }

            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
