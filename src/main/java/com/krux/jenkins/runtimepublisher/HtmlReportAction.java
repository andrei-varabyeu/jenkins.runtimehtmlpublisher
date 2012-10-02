package com.krux.jenkins.runtimepublisher;

import hudson.FilePath;
import hudson.model.Action;
import hudson.model.DirectoryBrowserSupport;

import java.io.IOException;

import javax.servlet.ServletException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * Base Action. Performed during build execution - publishes some html
 * 
 * @author Andrei Varabyeu
 * 
 */
public class HtmlReportAction implements Action {

	/** Html Report representation */
	private HtmlReport htmlReport;

	/** Build root directory */
	private FilePath rootDirectory;

	public HtmlReportAction(FilePath rootDirectory, HtmlReport htmlDescription) {
		this.htmlReport = htmlDescription;
		this.rootDirectory = rootDirectory;
	}

	public String getIconFileName() {
		return "graph.gif";
	}

	public String getDisplayName() {
		return htmlReport.getReportTitle();
	}

	public String getUrlName() {
		return htmlReport.getIndexPage();
	}

	public FilePath getIndexDirectoryPath() {
		return new FilePath(rootDirectory, htmlReport.getDirectory());
	}

	public boolean isPresent() throws IOException, InterruptedException {
		return getIndexDirectoryPath().exists();
	}

	public Long getPageCheckTimeout() {
		return this.htmlReport.getPageCheckTimeout() * 1000;
	}

	/**
	 * Serves HTML reports.
	 */
	public void doDynamic(StaplerRequest req, StaplerResponse rsp)
			throws IOException, ServletException {
		DirectoryBrowserSupport dbs = new DirectoryBrowserSupport(this,
				getIndexDirectoryPath(), htmlReport.getReportTitle(),
				"graph.gif", false);
		dbs.setIndexFileName(htmlReport.getIndexPage());
		dbs.generateResponse(req, rsp, this);

	}

}
