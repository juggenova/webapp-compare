package net.ghezzi.jugg.wcp.persistence.entity;

public enum ChoiceEnum {
	
	NO,
	YES,
	MAYBE;

	/**
	 * Ritorna i ChoiceEnum nell'ordine di visualizzazione
	 * @return
	 */
	public static ChoiceEnum[] getSortedValues() {
		return new ChoiceEnum[] {YES, NO, MAYBE};
	}
}


