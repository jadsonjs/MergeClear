# MergeClear

##### About:
MergeClear is an academic tool to deal with the problem of merge system that were cloned. This tool was part of a doctoral and master degree research.

![alt text](https://github.com/jadsonjs/MergeClear/blob/master/br.ufrn.spl.ev/DOCS/MergeClear.png)

##### Version: 

0.5.0 (not mature for commercial use)

##### Authors:

    Jadson Santos
    Uira Kulesza
    Daniel Alencar
    Gleydson Lima
    Fladson Gomes

##### How do I get set up?

Just chekout the project for the eclipse. You will need to install the EGit plugin in your eclipse first.

If you find some compile erros, please make sure the all pluing necessary dependences are installed in your eclipse (see dependence section).

See src/br-ufrn-spl-ev/DOC/Ev Tutorial.pdf file for more details about the tool configuration.

##### Configuration

   To Run the MergeClear you will need to configure two files:

    src/br/ufrn/slp/ev/config.properties
    src/br/ufrn/slp/ev/connections.properties

##### Dependencies

   JGit, SVNKit, Xstream, Eclipse JDT

##### Database configuration

Not necessary

##### How to run tests

Run the br.ufrn.spl.ev.TestAll.java class.

##### Deployment instructions

Right Click in the Project -> Run as -> Eclipse Application

##### Contribution guidelines

The following contributions can be made:

- Improve the algorithm of task dependence detections
- Create a wizard screen to edit configuration files
- Implement the merge and test phases of the approach 
- Test the GIT and GITHub integration
- Run this tool to analyze new system and confirm our data
- Improve the documentation
- Write new automatic tests for the tool

##### Writing tests

White your tests under the *src_test* source folder. Add your test to the br.ufrn.spl.ev.TestAll.java suite.

##### Who do I talk to?

    Jadson Santos - jadsonjs@gmail.com
    http://www.info.ufrn.br/wikisistemas/doku.php?id=research:assets
    http://www.dimap.ufrn.br/pt/


