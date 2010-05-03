//Copyright (c) 2006, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
//$Id$

package gov.nasa.jpl.oodt.cas.resource.tools;

//JDK imports
import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

//OODT imports
import gov.nasa.jpl.oodt.cas.resource.structs.JobSpec;
import gov.nasa.jpl.oodt.cas.resource.structs.exceptions.JobExecutionException;
import gov.nasa.jpl.oodt.cas.resource.system.XmlRpcResourceManagerClient;
import gov.nasa.jpl.oodt.cas.resource.util.JobBuilder;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * A tool to submit {@link Job} files generated in a particular directory to the
 * Resource Manager.
 * </p>.
 */
public final class JobSubmitter {

    /* our log stream */
    private static final Logger LOG = Logger.getLogger(JobSubmitter.class
            .getName());

    /* our res mgr client */
    private XmlRpcResourceManagerClient client = null;

    /* our job file filter */
    private static final FileFilter JOB_FILE_FILTER = new FileFilter() {

        public boolean accept(File file) {
            return file.isFile() && file.getName().endsWith(".xml");
        }

    };

    public JobSubmitter(URL rUrl) {
        client = new XmlRpcResourceManagerClient(rUrl);
    }

    public void submitJobFiles(File jobFileDir) {
        File[] jobFiles = jobFileDir.listFiles(JOB_FILE_FILTER);

        if (jobFiles != null && jobFiles.length > 0) {
            for (int i = 0; i < jobFiles.length; i++) {
                try {
                    String id = submitJobFile(jobFiles[i]);
                    LOG.log(Level.INFO, "Job Submitted: id: [" + id + "]");

                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.log(Level.WARNING, "Exception submitting job file: ["
                            + jobFiles[i] + "]: Message: " + e.getMessage());
                }
            }
        }

    }

    public void submitJobFiles(String jobFileDirPath) {
        submitJobFiles(new File(jobFileDirPath));
    }

    public String submitJobFile(File jobFile) throws JobExecutionException {
        return submitJobFile(jobFile.getAbsolutePath());
    }

    public String submitJobFile(String jobFilePath)
            throws JobExecutionException {
        JobSpec spec = JobBuilder.buildJobSpec(jobFilePath);
        return submitJob(spec);
    }

    public static void main(String[] args) throws Exception {
        String resMgrUrlStr = null;
        String jobFilePath = null, jobFileDirPath = null;
        String usage = "JobSubmitter --rUrl <resource mgr url> [options]\n"
                + "--file <job file path>\n" + "[--dir <job file dir path>]\n";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--rUrl")) {
                resMgrUrlStr = args[++i];
            } else if (args[i].equals("--file")) {
                jobFilePath = args[++i];
            } else if (args[i].equals("--dir")) {
                jobFileDirPath = args[++i];
            }
        }

        if (resMgrUrlStr == null
                || (jobFilePath == null && jobFileDirPath == null)) {
            System.err.println(usage);
            System.exit(1);
        }

        JobSubmitter submitter = new JobSubmitter(new URL(resMgrUrlStr));

        // if they specified --dir it takes precedence
        if (jobFileDirPath != null) {
            submitter.submitJobFiles(jobFileDirPath);
        } else {
            String jobId = submitter.submitJobFile(jobFilePath);
            LOG.log(Level.INFO, "Job Submitted: id: [" + jobId + "]");
        }

    }

    private String submitJob(JobSpec spec) throws JobExecutionException {
        return client.submitJob(spec.getJob(), spec.getIn());
    }

}