import hudson.*
import hudson.security.*
import jenkins.model.*
import java.util.*
import com.michelin.cio.hudson.plugins.rolestrategy.*
import com.synopsys.arc.jenkins.plugins.rolestrategy.*
import java.lang.reflect.*
import java.util.logging.*
import groovy.json.*

def env = System.getenv()

/**
 * ===================================
 *         
 *                Roles
 *
 * ===================================
 */
def globalRoleDevOps = "devops"
def globalRoleDSPlatform = "globaldsPlatform"
def projectRoleDSPlatform = "dsPlatform"
def globalRoleDataScience = "globaldataScience"
def projectRoleDataScience = "dataScience"
def globalRoleAirflow = "globalairflow"
def projectRoleAirflow = "airflow"
def globalRoleTopAds = "globaltopads"
def projectRoleTopAds = "topads"
/**
 * ===================================
 *         
 *           Users and Groups
 *
 * ===================================
 */
def access = [
  dataScience: ["anonymous"],
  dsPlatform: [],
  airflow: [],
  devops: [],
  topads: [],
  globaldataScience: ["anonymous"],
  globaldsPlatform: [],
  globalairflow: [],
  globaltopads: []
]

//def AUTHZ_JSON_FILE = "$WORKSPACE/roles.json"
//def AUTHZ_JSON_FILE = binding.variables.get('AUTHZ_JSON_FILE')

def AUTHZ_JSON_FILE = build.project.getWorkspace().child("roles.json") // fix this.
// echo $$AUTHZ_JSON_FILE

try { 
  if ( "${AUTHZ_JSON_FILE}" == "" && env.AUTHZ_JSON_FILE != null )
    // If default value is empty and env variable defined, use this env variable
    AUTHZ_JSON_FILE = "${env.AUTHZ_JSON_FILE}" 
} catch(ex) { }
try { 
  if ( "${AUTHZ_JSON_URL}" == "" && env.AUTHZ_JSON_URL != null )
    // If default value is empty and env variable defined, use this env variable
    AUTHZ_JSON_URL  = "${env.AUTHZ_JSON_URL}"  
} catch(ex) { }



if ( "${AUTHZ_JSON_FILE}" != "")  {
  println "Get role authorizations from file ${AUTHZ_JSON_FILE}"
  File f = new File("${AUTHZ_JSON_FILE}")
  def jsonSlurper = new JsonSlurper()
  def jsonText = f.getText()
  access = jsonSlurper.parseText( jsonText )
}
else if ( "${AUTHZ_JSON_URL}" != "") {
  println "Get role authorizations from URL ${AUTHZ_JSON_URL}"
  URL jsonUrl = new URL("${AUTHZ_JSON_URL}");
  access = new JsonSlurper().parse(jsonUrl);
}
else {
  println "Warning! Neither AUTHZ_JSON_FILE nor AUTHZ_JSON_URL specified!"
  println "Granting anonymous Data-Science access"
} 

/**
 * ===================================
 *         
 *           Permissions
 *
 * ===================================
 */

// TODO: drive these from a config file
def devopsPermissions = [
"hudson.model.Hudson.Administer",
"hudson.model.Hudson.Read"
]

def globaldataSciencePermissions = [
"hudson.model.Hudson.Read",
"hudson.model.Item.Read"
]

def dataSciencePermissions = [
"hudson.model.Item.Build",
"hudson.model.Item.Cancel",
"hudson.model.Item.Read"
]

def globaldsPlatformPermissions = [
"hudson.model.Hudson.Read",
"hudson.model.Item.Read"
]

def dsPlatformPermissions = [
"hudson.model.Item.Build",
"hudson.model.Item.Cancel",
"hudson.model.Item.Configure",
"hudson.model.Item.Create",
"hudson.model.Item.Delete",
"hudson.model.Item.Discover",
"hudson.model.Item.Move",
"hudson.model.Item.Read",
"hudson.model.Item.Workspace"
]

def globaltopadsPermissions = [
"hudson.model.Hudson.Read",
"hudson.model.Item.Read"
]

def topadsPermissions = [
"hudson.model.Item.Build",
"hudson.model.Item.Cancel",
"hudson.model.Item.Read"
]

def globalairflowPermissions = [
"hudson.model.Hudson.Read",
"hudson.model.Item.Read"
]

def airflowPermissions = [
"hudson.model.Item.Build",
"hudson.model.Item.Read"
]

def roleBasedAuthenticationStrategy = new RoleBasedAuthorizationStrategy()
Jenkins.instance.setAuthorizationStrategy(roleBasedAuthenticationStrategy)

Constructor[] constrs = Role.class.getConstructors();
for (Constructor<?> c : constrs) {
  c.setAccessible(true);
}

// Make the method assignRole accessible
Method assignRoleMethod = RoleBasedAuthorizationStrategy.class.getDeclaredMethod("assignRole", RoleType.class, Role.class, String.class);
assignRoleMethod.setAccessible(true);
println("Visibility of RoleBasedAuthorizationStrategy.assignRole changed!")

/**
 * ===================================
 *         
 *           Permissions
 *
 * ===================================
 */

Set<Permission> devopsPermissionSet = new HashSet<Permission>();
devopsPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    devopsPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> dataSciencePermissionSet = new HashSet<Permission>();
dataSciencePermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    dataSciencePermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> globaldataSciencePermissionSet = new HashSet<Permission>();
globaldataSciencePermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    globaldataSciencePermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}


Set<Permission> dsPlatformPermissionSet = new HashSet<Permission>();
dsPlatformPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    dsPlatformPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> globaldsPlatformPermissionSet = new HashSet<Permission>();
globaldsPlatformPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    globaldsPlatformPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> topadsPermissionSet = new HashSet<Permission>();
topadsPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    topadsPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> globaltopadsPermissionSet = new HashSet<Permission>();
globaltopadsPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    globaltopadsPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> airflowPermissionSet = new HashSet<Permission>();
airflowPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    airflowPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> globalairflowPermissionSet = new HashSet<Permission>();
globalairflowPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    globalairflowPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

/**
 * ===================================
 *         
 *      Permissions -> Roles
 *
 * ===================================
 */

// admins
Role devopsRole = new Role(globalRoleDevOps, devopsPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleType.Global, devopsRole);

Role globaldsPlatformRole = new Role(globalRoleDSPlatform, globaldsPlatformPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleType.Global, globaldsPlatformRole);

Role globaldataScienceRole = new Role(globalRoleDataScience, globaldataSciencePermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleType.Global, globaldataScienceRole);

Role globaltopadsRole = new Role(globalRoleTopAds, globaltopadsPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleType.Global, globaltopadsRole);

Role globalairflowRole = new Role(globalRoleAirflow, globalairflowPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleType.Global, globalairflowRole);

// builders
Role dsPlatformRole = new Role(projectRoleDSPlatform,"Arthur-Apis/.*", dsPlatformPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleType.Project, dsPlatformRole);

// anonymous read
Role dataScienceRole = new Role(projectRoleDataScience,"Arthur-Apis/arthur-apis-test/(devel|staging)", dataSciencePermissionSet); roleBasedAuthenticationStrategy.addRole(RoleType.Project, dataScienceRole);

// developers
Role topadsRole = new Role(projectRoleTopAds, "Arthur-Apis/ds-self-serve-parameterised-pipeline/devel", topadsPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleType.Project, topadsRole);

Role airflowRole = new Role(projectRoleAirflow,"Arthur-Apis/proxy_mb_pipeline_new", airflowPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleType.Project, airflowRole);

/**
 * ===================================
 *         
 *      Roles -> Groups/Users
 *
 * ===================================
 */

access.devops.each { l ->
  println("Granting DevOps role to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleType.Global, devopsRole, l);  
}

access.dsPlatform.each { l ->
  println("Granting DS Platform role to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleType.Global, globaldsPlatformRole, l);
  roleBasedAuthenticationStrategy.assignRole(RoleType.Project, dsPlatformRole, l);  
}

access.dataScience.each { l ->
  println("Granting Data Science to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleType.Global, globaldataScienceRole, l);
  roleBasedAuthenticationStrategy.assignRole(RoleType.Project, dataScienceRole, l);  
}

access.topads.each { l ->
  println("Granting TopAds to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleType.Global, globaltopadsRole, l);
  roleBasedAuthenticationStrategy.assignRole(RoleType.Project, topadsRole, l);  
}

access.airflow.each { l ->
  println("Granting Airflow to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleType.Global, globalairflowRole, l);
  roleBasedAuthenticationStrategy.assignRole(RoleType.Project, airflowRole, l);  
}

Jenkins.instance.save()
