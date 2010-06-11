/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package gov.nasa.jpl.oodt.cas.resource.jobrepo;

//OODT imports
import gov.nasa.jpl.oodt.cas.resource.structs.JobSpec;
import gov.nasa.jpl.oodt.cas.resource.structs.exceptions.JobRepositoryException;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * An interface for persisting {@link JobSpec}s
 * </p>.
 */
public interface JobRepository {

  public String addJob(JobSpec spec) throws JobRepositoryException;

  public void updateJob(JobSpec spec) throws JobRepositoryException;

  public void removeJob(JobSpec spec) throws JobRepositoryException;
  
  public JobSpec getJobById(String jobId) throws JobRepositoryException;

  public String getStatus(JobSpec spec) throws JobRepositoryException;

  public boolean jobFinished(JobSpec spec) throws JobRepositoryException;

}
