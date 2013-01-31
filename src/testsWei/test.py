#! /usr/bin/env python

import os
from optparse import OptionParser

def get_env():
    
    currentDir = os.path.abspath('.')
    projectDir = os.path.abspath('..')
    executeDir = projectDir + "/bin/frontend/"
    scriptPath = projectDir + "/test/"
    classPath = (projectDir + "/bin/", "$MAXINE_HOME/com.oracle.max.base/bin")
    
    return {
            'executable'    : executeDir,
            'project'       : projectDir,
            'script'        : scriptPath,
            'cp'            : classPath,
            'current'       : currentDir
            }
    
def get_executable(env=get_env(), key='executable'):
    return env[key]

def get_classpath(env=get_env(), key='cp'):
  return ':'.join(env[key])

def get_scriptpath(env=get_env(), key='script'):
    return env[key]

def do_execute(arg, host_vm_opts='', client_vm_opts='', exe_class = "frontend.Execute"):
    """
    this function executes one testing script specified by arg
    """
    java = 'java'
    script = get_scriptpath() + arg
    print script
    command = "%s %s -cp %s %s %s %s" % (java,
                                        host_vm_opts,
                                        get_classpath(),
                                        exe_class,
                                        client_vm_opts,
                                        script)
    
    print "Executing ", command 
    exit_code = os.system(command)
    
def enum_scripts(filter_fn=lambda x: True, script_path=get_scriptpath(), follow_mbs_ignore=True):
    """
    This function is used to enumerate the scripts found in the script path. At the
    same time you can supply a custom "filter_fn" function to filter out unwanted entries
    from the input directory.
    NOTE: If this function finds a file called ".ignore" in the directory specified by
    the named "script_path" argument, it will exclude all files listed in there.
    """
    ignore_file= "%s/.ignore" % (script_path)
    exclude= [] 
    if follow_mbs_ignore and os.path.exists(ignore_file):
        with open(ignore_file) as input:
            for l in input:
                exclude.append( l.strip() )

    ## for s in os.listdir( script_path ):
    ##     if filter_fn(s) and s not in exclude:
    ##         yield "%s/%s" % (script_path, s)
    for (base, _, files) in os.walk(script_path):
        if not files:
            continue
        for f in files:
            if filter_fn(f) and f not in exclude:
#                yield "%s/%s" % (base, f)
                yield f
    
    
def run_all_tests(scripts, host_opts='', client_opts='', exe_class = "frontend.Execute"):
    for s in enum_scripts(filter_fn= lambda x: not x.startswith('.') ):
        print "Running ... %s" % (s)
        do_execute(s, host_opts, client_opts)

def parse_args():
    p = OptionParser()
    p.add_option("-o", "--host",    action="store",   dest="host",    default="",     help="options to pass to the Host VM")
    p.add_option("-O", "--client",  action="store",   dest="client",  default="",     help="options to pass to the Client VM")
    p.add_option("-x", "--execute", action="store",   dest="execute", default="",     help="executes a specified script: {" + ", ".join(os.listdir(get_scriptpath())) + "}")
    p.add_option("-r", "--run",     action="store_true",    dest="run",     default=False,  help="run all scripts")
    return p.parse_args()


if __name__ == "__main__": 
    (opts, args) = parse_args()
    
    tasks = {
             'execute'  : do_execute,
             'run'      : run_all_tests
             }
    
    for (key, iter_fn) in tasks.iteritems():
        if hasattr(opts, key) and getattr(opts, key):
            # test
#            print("key and iter_fn ", key, iter_fn)
#            print getattr(opts, key)
            iter_fn(getattr(opts, key), opts.host, opts.client)
    
    
    
    
