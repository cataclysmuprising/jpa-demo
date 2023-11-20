package com.example.compressor;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Main {
	private static final Logger logger = LogManager.getLogger(Main.class);
	// compress လုပ်မယ့် module အားလုံးရဲ့ root directory. များသောအားဖြင့် UI parent module
	private static final String PROJECT_ROOT_DIR = "D:/projects/jpa-demo";
	// compress လုပ်မယ့် module တစ်ခုချင်းစီအောက်မှာရှိတဲ့ root static resource directory
	private static final String STATIC_RSS_PATH = "src/main/resources/static";
	// compression လုပ်တဲ့ အခါမှာ သုံးမယ့် root temporary directory location
	private static final String STATIC_DIST_PATH = "D:/temp/static-resources";
	// compression လုပ်ပြီးသွားတဲ့ အခါမှာ နောက်ဆုံး output ကို ထားမယ့် root directory
	private static final String ROOT_OUT_DIR = "D:/project-releases/jpa-demo/static-resources/";
	private static final Options o = new Options();
	private static final Charset charset = StandardCharsets.UTF_8;
	// project အလိုက် output directory တွေကွဲသွားမယ်။
	private static String outputDirectory;

	public static void main(String[] args) throws IOException {

		String projectName = ArrayUtils.isNotEmpty(args) ? args[0] : "jpa-demo-backend";

		String devApplicationUrl = "--------------------------";
		String prdApplicationUrl = "";

		Properties deploymentProperties = loadDeploymentProperties();

		// define project URLs for each war file
		if (projectName.equals("jpa-demo-backend")) {
			devApplicationUrl = deploymentProperties.get("app-dev-url").toString();
			prdApplicationUrl = deploymentProperties.get("app-prd-url").toString();
		}
		// output directory ကို project name အလိုက် သူ့ static file နဲ့သူ ထုတ်မယ်။
		outputDirectory = ROOT_OUT_DIR + projectName + "/static";
		logger.info("xxxxxxxxxxxxxx Start Compression for '{}' project xxxxxxxxxxxxxx", projectName);

		// compress လုပ်မယ့် module တစ်ခုချင်းစီအောက်မှာရှိတဲ့ root static resource directory ရဲ့ absolute path. သူ့ sub-module name နဲ့သူ ထားမယ်။
		String sourceRssPath = PROJECT_ROOT_DIR + "/" + projectName + "/" + STATIC_RSS_PATH;
		// compression လုပ်တဲ့ အခါမှာ သုံးမယ့် root temporary directory location ရဲ့ absolute path. သူ့ sub-module name နဲ့သူ လုပ်မယ်။
		String distRssPath = STATIC_DIST_PATH + "/" + projectName;

		logger.info("Step 1 : ==> Prepare directories and static resources for compression.");
		prepare(sourceRssPath, distRssPath);

		// compression မလုပ်ခင်မှာ အရင်ဆုံး css files တွေထဲမှာရှိတဲ့ application URL ကို လိုက်ပြောင်းမယ်။
		// လက်ရှိမှာက development application ရဲ့ url နဲ့ css file တွေက လုပ်ထားတယ်။
		logger.info("Step 2 : ==> Change the application URLs of static resource files.");
		setApplicationUrl(distRssPath, devApplicationUrl, prdApplicationUrl);

		// common css file တွေနဲ့ js file တွေကို အရင် compression လုပ်မယ်။
		logger.info("Step 3 : ==> Compress 'Common' JavaScript and StyleSheet Resources.");
		compressCommonResources(distRssPath);

		// app css file တွေနဲ့ js file တွေကို compression လုပ်မယ်။
		logger.info("Step 3 : ==> Compress 'App' JavaScript and StyleSheet Resources.");
		compressAppResources(distRssPath);

		logger.info("xxxxxxxxxxxxxx Finished Compression Static Resources for project '{}' xxxxxxxxxxxxxx", projectName);
	}

	private static void prepare(String sourceRssPath, String distRssPath) throws IOException {
		// အရင်ဆုံး temporary directory location အောက်က ဖိုင် အဟောင်းတွေကို အရင်ရှင်းမယ်။
		logger.debug(">>> Clean temporary directory for path : '{}'", distRssPath);
		FileUtils.cleanDirectory(createOrRetrieveFile(distRssPath));
		logger.debug(">>> Clean output directory for path : '{}'", outputDirectory);
		FileUtils.cleanDirectory(createOrRetrieveFile(outputDirectory));
		// source static resources တွေကို temporary directory ဆီကို ကူးထည့်မယ်။ မူရင်း source directory အောက်မှာ မလုပ်ဘူး။
		logger.debug(">>> Copy css and js file directories from directory '{}' to temporary directory '{}'.", sourceRssPath, distRssPath);
		Predicate<Path> cssAndJSfilesOnlyPredicate = t -> t.toString().endsWith("css") || t.toString().endsWith("js");
		try (Stream<Path> paths = Files.walk(Paths.get(sourceRssPath), 1)) {
			paths.filter(Files::isDirectory).filter(cssAndJSfilesOnlyPredicate).forEach(path -> {
				try {
					FileUtils.copyDirectoryToDirectory(path.toFile(), Paths.get(distRssPath).toFile());
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		// css file တွေ ၊ js file တွေ မဟုတ်တဲ့ အခြား static resources တွေကို output directory ထဲ copy ကူးထည့်မယ်။
		logger.debug(">>> Copy non-css and non-js static resources to output directory '{}'.", outputDirectory);
		Predicate<Path> otherStaticRssPredicate = t -> !t.toString().endsWith("css") && !t.toString().endsWith("js") && !t.toString().endsWith("static");
		try (Stream<Path> paths = Files.walk(Paths.get(sourceRssPath), 1)) {
			paths.filter(Files::isDirectory).filter(otherStaticRssPredicate).forEach(path -> {
				try {
					FileUtils.copyDirectoryToDirectory(path.toFile(), Paths.get(outputDirectory).toFile());
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	private static void setApplicationUrl(String distRssPath, String devApplicationUrl, String prdApplicationUrl) throws IOException {
		// css file တွေချည်းပဲ လုပ်မှာ။
		Predicate<Path> cssRssPredicate = t -> t.toString().endsWith(".css");
		// css folder တစ်ခုတည်းကိုပဲ လုပ်မှာ။
		Path cssDirectory = Paths.get(distRssPath + "/css");
		try (Stream<Path> paths = Files.walk(cssDirectory)) {
			paths.filter(Files::isRegularFile).filter(cssRssPredicate).forEach(path -> {
				try {
					// css file ကို String အနေနဲ့ အရင် ဖတ်လိုက်မယ်။
					String content = Files.readString(path, charset);
					// ပြီးမှရလာတဲ့ String ထဲက development-mode URL တွေကို production-mode URL တွေနဲ့ လိုက်အစားထိုးမယ်။
					content = content.replaceAll(devApplicationUrl, prdApplicationUrl);
					// နောက်ဆုံး replace လုပ်သား String ကို မူလ ဖိုင်ထဲ ပြန်ရိုက်ထည့်မယ်။
					Files.writeString(path, content, charset);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	public static void compressCommonResources(String distRssPath) throws IOException {
		// common resource file တွေကို minify ပဲ လုပ်ပြီး output အဖြစ်ထားမယ့် directory ထဲကို တန်းထည့်မယ်။ ဘာဖိုင်တွေနဲ့မှ concat မလုပ်ဘူး။
		// CSS file သပ်သပ် JS file သပ်သပ် compress လုပ်မယ်။

		// source ရဲ့ directory structure. output ထုတ်တဲ့ အခါမှာလည်း source directory structure အတိုင်းဖြစ်အောင် လုပ်ဖို့လိုတယ်။
		String commonCsspath = "/css/common";
		Path commonCSSDir = Paths.get(distRssPath + commonCsspath);
		logger.info(">>> Compressing common CSS files");
		try (Stream<Path> paths = Files.walk(commonCSSDir)) {
			paths.filter(Files::isRegularFile).forEach(path -> {
				// css file ရဲ့ name ။ ဥပမာ - theme.css,app.css
				String fileName = path.getFileName().toString();
				File outputDir = createOrRetrieveFile(outputDirectory + commonCsspath);
				File sourceFile = path.toFile();
				File destinationFile;
				if (fileName.endsWith(".min.css")) {
					destinationFile = new File(outputDir + "/" + fileName);
					try {
						FileUtils.copyFile(sourceFile, destinationFile);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				else {
					destinationFile = new File(outputDir + "/" + fileName.replace(".css", ".min.css"));
					try {
						compressCss(sourceFile, destinationFile);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}

		// source ရဲ့ directory structure. output ထုတ်တဲ့ အခါမှာလည်း source directory structure အတိုင်းဖြစ်အောင် လုပ်ဖို့လိုတယ်။
		String commonJSpath = "/js/common";
		Path commonJSDir = Paths.get(distRssPath + commonJSpath);
		logger.info(">>> Compressing common Javascript files");
		try (Stream<Path> paths = Files.walk(commonJSDir)) {
			paths.filter(Files::isRegularFile).forEach(path -> {
				// js file ရဲ့ name ။ ဥပမာ - theme.js,app.js
				String fileName = path.getFileName().toString();
				File outputDir = createOrRetrieveFile(outputDirectory + commonJSpath);
				File sourceFile = path.toFile();
				File destinationFile;
				if (fileName.endsWith(".min.js")) {
					destinationFile = new File(outputDir + "/" + fileName);
					try {
						FileUtils.copyFile(sourceFile, destinationFile);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				else {
					destinationFile = new File(outputDir + "/" + fileName.replace(".js", ".min.js"));
					try {
						compressJavaScript(sourceFile, destinationFile);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void compressAppResources(String distRssPath) throws IOException {
		// common resource file တွေကို minify ပဲ လုပ်ပြီး output အဖြစ်ထားမယ့် directory ထဲကို တန်းထည့်မယ်။ ဘာဖိုင်တွေနဲ့မှ concat မလုပ်ဘူး။
		// CSS file သပ်သပ် JS file သပ်သပ် compress လုပ်မယ်။

		// source ရဲ့ directory structure. output ထုတ်တဲ့ အခါမှာလည်း source directory structure အတိုင်းဖြစ်အောင် လုပ်ဖို့လိုတယ်။
		String commonCsspath = "/css/app";
		Path commonCSSDir = Paths.get(distRssPath + commonCsspath);
		logger.info(">>> Compressing app CSS files");
		try (Stream<Path> paths = Files.walk(commonCSSDir)) {
			paths.filter(Files::isRegularFile).forEach(path -> {
				// css file ရဲ့ name ။ ဥပမာ - theme.css,app.css
				String fileName = path.getFileName().toString();
				File outputDir = createOrRetrieveFile(outputDirectory + commonCsspath);
				File sourceFile = path.toFile();
				File destinationFile;
				if (fileName.endsWith(".min.css")) {
					destinationFile = new File(outputDir + "/" + fileName);
					try {
						FileUtils.copyFile(sourceFile, destinationFile);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				else {
					destinationFile = new File(outputDir + "/" + fileName.replace(".css", ".min.css"));
					try {
						compressCss(sourceFile, destinationFile);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}

		// source ရဲ့ directory structure. output ထုတ်တဲ့ အခါမှာလည်း source directory structure အတိုင်းဖြစ်အောင် လုပ်ဖို့လိုတယ်။
		String commonJSpath = "/js/app";
		Path commonJSDir = Paths.get(distRssPath + commonJSpath);
		logger.info(">>> Compressing app Javascript files");
		try (Stream<Path> paths = Files.walk(commonJSDir)) {
			paths.filter(Files::isRegularFile).forEach(path -> {
				// js file ရဲ့ name ။ ဥပမာ - theme.js,app.js
				String fileName = path.getFileName().toString();
				File outputDir = createOrRetrieveFile(outputDirectory + commonJSpath);
				File sourceFile = path.toFile();
				File destinationFile;
				if (fileName.endsWith(".min.js")) {
					destinationFile = new File(outputDir + "/" + fileName);
					try {
						FileUtils.copyFile(sourceFile, destinationFile);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				else {
					destinationFile = new File(outputDir + "/" + fileName.replace(".js", ".min.js"));
					try {
						compressJavaScript(sourceFile, destinationFile);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	private static void compressCss(File sourceFile, File destinationFile) throws IOException {
		logger.debug("Source File => " + sourceFile.getAbsolutePath());
		logger.debug("Destination File => " + destinationFile.getAbsolutePath());
		Reader in = new InputStreamReader(new FileInputStream(sourceFile), o.charset);
		CssCompressor compressor = new CssCompressor(in);
		in.close();
		destinationFile.createNewFile();
		Writer out = new OutputStreamWriter(new FileOutputStream(destinationFile), o.charset);
		compressor.compress(out, o.lineBreakPos);
		out.flush();
		out.close();
	}

	private static void compressJavaScript(File sourceFile, File destinationFile) throws IOException {
		logger.debug("Source File => " + sourceFile.getAbsolutePath());
		logger.debug("Destination File => " + destinationFile.getAbsolutePath());
		try {
			Reader in = new InputStreamReader(new FileInputStream(sourceFile), o.charset);
			JavaScriptCompressor compressor = new JavaScriptCompressor(in, new YuiCompressorErrorReporter());
			in.close();
			in = null;
			destinationFile.createNewFile();
			Writer out = new OutputStreamWriter(new FileOutputStream(destinationFile), o.charset);
			compressor.compress(out, o.lineBreakPos, o.munge, o.verbose, o.preserveAllSemiColons, o.disableOptimizations);
			out.flush();
			out.close();
		}
		catch (EvaluatorException e) {
			FileChannel src = new FileInputStream(sourceFile).getChannel();
			FileChannel dest = new FileOutputStream(destinationFile).getChannel();
			dest.transferFrom(src, 0, src.size());
		}
	}

	private static void concatFiles(List<String> filepaths, Path targetFile) {
		try {
			targetFile.toFile().createNewFile();
			for (String filePath : filepaths) {
				List<String> lines = Files.readAllLines(Paths.get(filePath), charset);
				Files.write(targetFile, lines, charset, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void compressPageResource(String distRssPath, List<String> resourceFilePaths, ResourceType resourceType) {
		String pageFile = (resourceType == ResourceType.JS) ? "page.js" : "page.css";
		String concatFile = (resourceType == ResourceType.JS) ? "/concat.js" : "/concat.css";
		String minFile = (resourceType == ResourceType.JS) ? "/page.min.js" : "/page.min.css";

		if (!resourceFilePaths.isEmpty()) {
			Path concatenatedFile = null;
			File outputDir = null;
			for (int i = resourceFilePaths.size() - 1; i >= 0; i--) {
				String resource = resourceFilePaths.get(i);
				if (resource.endsWith(pageFile)) {
					outputDir = createOrRetrieveFile(resource.replace(distRssPath, outputDirectory).replace(pageFile, ""));
					concatenatedFile = Paths.get(outputDir.getAbsolutePath() + concatFile);
					break;
				}
			}
			if (concatenatedFile != null) {
				try {
					concatenatedFile.toFile().createNewFile();
					for (String jsRss : resourceFilePaths) {
						List<String> lines = Files.readAllLines(Paths.get(jsRss), charset);
						Files.write(concatenatedFile, lines, charset, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
					}
					Path destinationFile = Paths.get(outputDir.getAbsolutePath() + minFile);
					destinationFile.toFile().createNewFile();
					if (resourceType == ResourceType.JS) {
						compressJavaScript(concatenatedFile.toFile(), destinationFile.toFile());
					}
					else {
						compressCss(concatenatedFile.toFile(), destinationFile.toFile());
					}
					try {
						Files.deleteIfExists(concatenatedFile);
					}
					catch (Exception e) {
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static File createOrRetrieveFile(String target) {
		Path path = Paths.get(target);
		if (Files.notExists(path)) {
			try {
				return Files.createDirectories(path).toFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return path.toFile();
	}

	private static Properties loadDeploymentProperties() throws IOException {
		logger.info(">>> Loading Migration Properties.....");
		Properties deploymentProperties = new Properties();
		try (InputStream migrationStream = Main.class.getResourceAsStream("/deployment.properties")) {
			deploymentProperties.load(migrationStream);
		}
		return deploymentProperties;
	}

	enum ResourceType {
		JS, CSS
	}

	public static class Options {
		String charset = "UTF-8";
		int lineBreakPos = -1;
		boolean munge = true;
		boolean verbose = false;
		boolean preserveAllSemiColons = false;
		boolean disableOptimizations = false;
	}

	public static class YuiCompressorErrorReporter implements ErrorReporter {
		@Override
		public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
			if (line < 0) {
				logger.warn(message);
			}
			else {
				logger.warn(line + ':' + lineOffset + ':' + message);
			}
		}

		@Override
		public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
//			System.err.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//			System.err.println(message);
//			System.err.println(line);
//			System.err.println(lineSource);
//			System.err.println(lineOffset);
//			System.err.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//			if (line < 0) {
//				logger.error(message);
//			}
//			else {
//				logger.error(line + ':' + lineOffset + ':' + message);
//			}
		}

		@Override
		public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
			error(message, sourceName, line, lineSource, lineOffset);
			return new EvaluatorException(message);
		}
	}
}
