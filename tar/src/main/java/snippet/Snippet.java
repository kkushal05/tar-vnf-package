package snippet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class Snippet {

	public static void main(String a[]) throws FileNotFoundException, IOException {
		Snippet s = new Snippet();
		s.CreateTarGZ();
	}

	public void CreateTarGZ() throws FileNotFoundException, IOException {

		FileOutputStream fOut = null;
		BufferedOutputStream bOut = null;
		GzipCompressorOutputStream gzOut = null;
		TarArchiveOutputStream tOut = null;
		String dirPath = "/home/sdn/vOpenNAT";
		String tarGzPath = "/home/sdn/vOpenNAT.tar.gz";

		try {
			System.out.println(new File(".").getAbsolutePath());

			fOut = new FileOutputStream(new File(tarGzPath));
			bOut = new BufferedOutputStream(fOut);
			gzOut = new GzipCompressorOutputStream(bOut);
			tOut = new TarArchiveOutputStream(gzOut);
			tOut.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_POSIX);
			tOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

			addFileToTarGz(tOut, dirPath, "");
		} finally {
			tOut.finish();
			tOut.close();
			gzOut.close();
			bOut.close();
			fOut.close();
		}
	}

	private void addFileToTarGz(TarArchiveOutputStream tOut, String dirPath, String base) throws IOException {
		File f = new File(dirPath);
		System.out.println(f.exists());
		String entryName = base + f.getName();
		TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);
		tOut.putArchiveEntry(tarEntry);

		if (f.isFile()) {
			IOUtils.copy(new FileInputStream(f), tOut);
			tOut.closeArchiveEntry();
		} else {
			tOut.closeArchiveEntry();
			File[] children = f.listFiles();
			if (children != null) {
				for (File child : children) {
					System.out.println(child.getName());
					addFileToTarGz(tOut, child.getAbsolutePath(), entryName + "/");
				}
			}
		}
	}
}
