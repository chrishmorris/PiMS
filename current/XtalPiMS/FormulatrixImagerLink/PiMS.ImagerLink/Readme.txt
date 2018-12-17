PiMS.ImagerLink Solution
========================
Jon Diprose, 15/08/2011


PiMS.ImagerLink is a VS2008 Solution that contains four projects. The projects consist of
the PiMS implementation of the Formulatrix.Integrations.ImagerLink API, an application to
upload image metadata to PiMS and some test code. Below is a summary of the purpose of
the different projects. See the projects themselves for more information.

NB: Both OPPF.Integrations.ImagerLink and ImageUploader require the OPPFImagerLink.xml
configuration file. This file contains passwords and so has not been checked in (the file
is ignored). OPPF.Integrations.ImagerLink contains an OPPFImagerLink.xml.sample which can
be used as a template for the creation of this missing file.

Jon is using the AnkhSVN VS plugin to provide svn support within VS, available from:

http://ankhsvn.open.collab.net/

You should install this plugin prior to checking anything out. AnkhSVN quite good at not
checking in build directories and local config files but I had to use another svn client
(TortoiseSVN) to actually ignore these files.


OPPF.Integrations.ImagerLink
============================

This project is the implementation of the Formulatrix.Integrations.ImagerLink API. The
output of the project is a dll which is to be installed as an Extension into the relevant
RockImager and RockImagerProcessor applications, along with some config files and
required libraries.

TestOPPFImagerLink
==================

This project contains unit tests for OPPF.Integrations.ImagerLink. Unlike Java junit
tests, it appears to be normal practice (and necessary) to split this into a separate
project.

OPPFImagerLinkTester
====================

This project provides a GUI-based application through which the
OPPF.Integrations.ImagerLink implementation can be exercised and the results visualized.
Whilst I am obviously unable to say for certain how RockImager/RockImagerProcessor will
actually call the API methods, this will at least allow one to check that the methods
behave as expected without having to install VS on the relevant PCs.

ImageUploader
=============

The RockImagerProcessor integration does not directly update PiMS about each image.
Instead the image file and and an xml document containing the image metadata are written
out. ImageUploader processes these xml documents and updates PiMS.
