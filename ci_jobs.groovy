// Define the repos that need to be build
def repos = [ 'webgoat' ]

// Create a multibranch pipeline job for each of the repos
for (repo in repos)
{
  multibranchPipelineJob("${repo}") {

    // Build master as well as feature branches 
    branchSources {
      git {
        id("test")
        remote("https://github.com/Theocharis1/${repo}.git")
        includes("master feature/*")
      }
    }
    // Check every minute for scm changes as well as new or deleted branches
    triggers {
      periodic(1)
    }
    // Do not keep build jobs for deleted branches
    orphanedItemStrategy {
      discardOldItems {
        numToKeep(0)
      }
    }
  }
  // Automatically queue the job after the initial creation
  if (!jenkins.model.Jenkins.instance.getItemByFullName("${repo}")) {
    queue("${repo}")
  }
}
