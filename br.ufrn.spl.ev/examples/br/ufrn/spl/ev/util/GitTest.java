package br.ufrn.spl.ev.util;

import br.ufrn.spl.ev.engines.connectors.Connector;
import br.ufrn.spl.ev.engines.connectors.ConnectorFactory;
import br.ufrn.spl.ev.engines.connectors.RepositoryConnector;
import br.ufrn.spl.ev.engines.miners.MinerFactory.MergeSide;

public class GitTest {

	public static void main(String[] args) throws Exception {
		
		MergeSide mergeside = MergeSide.SOURCE;
		ConnectorFactory factory = new ConnectorFactory();
		RepositoryConnector connector = factory.getRepositoryConnector(mergeside, true);
		System.out.println(connector.toString());

		

	}

}
