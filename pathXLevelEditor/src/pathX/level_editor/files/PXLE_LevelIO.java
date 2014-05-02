/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathX.level_editor.files;

import java.io.File;
import pathX.level_editor.model.PXLE_Level;
import pathX.level_editor.model.PXLE_Model;

/**
 *
 * @author McKillaGorilla
 */
public interface PXLE_LevelIO
{
    public boolean loadLevel(File levelFile, PXLE_Model model);
    public boolean saveLevel(File levelFile, PXLE_Level levelToSave);
}
