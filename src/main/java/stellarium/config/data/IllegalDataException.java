package stellarium.config.data;

import stellarium.config.ICfgMessage;
import stellarium.lang.CPropLangUtil;

public class IllegalDataException extends RuntimeException {

	public IllegalDataException(String lvl, String title, ICfgMessage msg) {
		super(String.format("<%s>[%s]: %s", lvl, CPropLangUtil.getLocalizedFromID(title),
					CPropLangUtil.getLocalizedMessage(msg)));
	}

}
