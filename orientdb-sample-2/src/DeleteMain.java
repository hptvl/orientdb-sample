import orientdb.OrientDBDeleteQuery;
import orientdb.OrientDBFactory;

public class DeleteMain {

	public static void main(String[] args) {
		OrientDBFactory.init("remote:192.168.10.212/test-2", "root", "igloosec", 1, 10, true);
		try {
			long sTime = System.nanoTime();
			OrientDBDeleteQuery.deleteAllIPSs();
			long eTime = System.nanoTime();
			long elapsedTime = eTime - sTime;
			System.out.println("##########################################");
			System.out.println("Query Elapsed: " + elapsedTime + " nano seconds");
			System.out.println("Query Seconds: " + elapsedTime / 1000000000.0);
		} finally {
			OrientDBFactory.close();
		}
	}
}