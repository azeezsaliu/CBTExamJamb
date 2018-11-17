/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cbtexamjamb;

import com.healthmarketscience.jackcess.CryptCodecProvider;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import java.io.File;
import java.io.IOException;

import net.ucanaccess.jdbc.JackcessOpenerInterface;

/**
 *
 * @author OLAWALE
 */
public class CryptCodecOpener implements JackcessOpenerInterface{

    @Override
    public Database open(File file, String pwd) throws IOException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        DatabaseBuilder db = new DatabaseBuilder(file);
        db.setAutoSync(false);
        db.setCodecProvider(new CryptCodecProvider(pwd));
        db.setReadOnly(false);
        return db.open();

    }
    
}
