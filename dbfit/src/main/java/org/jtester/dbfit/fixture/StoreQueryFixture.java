package org.jtester.dbfit.fixture;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jtester.dbfit.environment.DBEnvironment;

import dbfit.environment.DbEnvironmentFactory;
import dbfit.util.DataTable;
import fit.Fixture;
import fit.Parse;

public class StoreQueryFixture extends Fixture {
	private DBEnvironment dbEnvironment;
	private String query;
	private String symbolName;

	public StoreQueryFixture() {
		dbEnvironment = DbEnvironmentFactory.getDefaultEnvironment();
	}

	public StoreQueryFixture(DBEnvironment environment, String query, String symbolName) {
		this.dbEnvironment = environment;
		this.query = query;
		this.symbolName = symbolName;
	}

	public void doTable(Parse table) {
		if (query == null || symbolName == null) {
			if (args.length < 2)
				throw new UnsupportedOperationException(
						"No query and symbol name specified to StoreQuery constructor or argument list");
			query = args[0];
			symbolName = args[1];
		}
		if (symbolName.startsWith(">>"))
			symbolName = symbolName.substring(2);
		try {
			PreparedStatement st = dbEnvironment.createStatementWithBoundFixtureSymbols(query);
			ResultSet rs = st.executeQuery();
			DataTable dt = new DataTable(rs);
			dbfit.util.SymbolUtil.setSymbol(symbolName, dt);
		} catch (SQLException sqle) {
			throw new Error(sqle);
		}
	}
}