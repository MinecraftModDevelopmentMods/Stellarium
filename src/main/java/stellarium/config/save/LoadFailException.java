package stellarium.config.save;

import java.util.IllegalFormatException;

import stellarium.config.ICfgMessage;
import stellarium.lang.CPropLangUtil;

public class LoadFailException extends IllegalArgumentException {
	
    public LoadFailException(String title, ICfgMessage msg) {
        super(String.format("Load Failed for Configuration [%s];\n Cause: %s", CPropLangUtil.getLocalizedFromID(title),
				CPropLangUtil.getLocalizedMessage(msg)));
    }

}
