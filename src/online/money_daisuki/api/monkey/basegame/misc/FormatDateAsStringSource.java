package online.money_daisuki.api.monkey.basegame.misc;

import java.text.DateFormat;
import java.util.Date;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;

public final class FormatDateAsStringSource implements DataSource<String> {
	private final DataSource<? extends DateFormat> dateFormatSource;
	private final DataSource<? extends Date> dateSource;
	
	public FormatDateAsStringSource(final DataSource<? extends DateFormat> dateFormatSource,
			final DataSource<? extends Date> dateSource) {
		this.dateFormatSource = Requires.notNull(dateFormatSource, "dateFormatSource == null");
		this.dateSource = Requires.notNull(dateSource, "dateSource == null");
	}
	@Override
	public String source() {
		return(dateFormatSource.source().format(dateSource.source()));
	}
}
