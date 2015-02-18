package stellarium.config.data;

import stellarium.construct.CPropLangUtil;

public class IllegalDataException extends RuntimeException {

	public IllegalDataException(String lvl, String title, String msg) {
		super(String.format("<%s>[%s]: %s", lvl, title,
					CPropLangUtil.getLocalizedString(msg)));
	}

}
