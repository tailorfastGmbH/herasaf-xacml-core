[![Build Status](https://prolutions.visualstudio.com/herasaf/_apis/build/status/prolutionsGmbH.herasaf-xacml-core?branchName=main)](https://prolutions.visualstudio.com/herasaf/_build/latest?definitionId=8&branchName=main)

Welcome to HERAS-AF XACML Core.
HERAS-AF XACML Core is an open source XACML 2.0 implementation.


[TOC]

## Features ##

### XACML 2.0 ###
* All XACML 2.0 features needed for conformance
* Obligations (including AttributeAssignments)

### Beyond XACML 2.0 ###
* Factory for creating a simple PDP that initializes all minimal needed components automatically.
* PDP for evaluating XACML 2.0 requests.
* A simple PolicyRepository that holds the currently deployed Evaluatables in the memory for evaluation.
* Possibility of loading local references.
* Various extension Points for adding new functions, combining algorithms, data types.
* Marshalling capabilities (JAXB) from and to various formats (File, Streams, ...) of Policies, PolicySets, Requests and Responses.
* Various configuration possibilities through the SimplePDPFactory.

## Developer's Resources ##
If you have any quetions regarding how to use the herasaf-xacml-core or about the project use [Stackoverflow](http://stackoverflow.com/) with the tags **#herasaf** or **#xacml**.

### herasaf-xacml-core version 2.x

The current release is available from maven central.
```xml
<dependency>
    <groupId>org.herasaf.xacml.core</groupId>
    <artifactId>herasaf-xacml-core</artifactId>
    <version>2.x.x</version>
</dependency>
```

Requirements
* Java 8
* J2EE 8 with javax.xml.bind namespace for JAXB

### herasaf-xacml-core version 3.x

The current release is available from maven central.
```xml
<dependency>
    <groupId>org.herasaf.xacml.core</groupId>
    <artifactId>herasaf-xacml-core</artifactId>
    <version>3.x.x</version>
</dependency>
```

Requirements
* Java 11
* J2EE 10+ with jakarta.xml.bind namespace for JAXB

## Who is using? ##
||
| ------------- |
| [Nedap](http://www.nedap.com/) is a company based in the Netherlands that provides solutions for physical access control. Nedap's new generation products will build up on XACML as access control language. HERAS-AF XACML Core is integrated to enforce the physical access based on the deployed policies. |
| [Forcare B.V.](http://www.forcare.nl/) is a Dutch software company providing software products and services for healthcare. Its focus is interoperability between healthcare IT systems. Forcare B.V. provides products and services to allow existing systems to (electronically) interconnect to share and exchange patient data.  |
| [PrimeLife](http://primelife.ercim.eu/) will address the core privacy and trust issues pertaining to the challenges of how to protect privacy in emerging Internet applications such as collaborative scenarios and virtual communities; and how to maintain life-long privacy. Its long-term vision is to counter the trend to life-long personal data trails without compromising on functionality. It will build upon and expand the FP6 project Prime that has shown how privacy technologies can enable citizens to execute their legal rights to control personal information in on-line transactions. The main objective of the project is to bring sustainable privacy and identity management to future networks and services. PrimeLife is developped by IBM Research and various other companies and universities: Consortium. |
| HERAS-AF is built into the core of a beta version of an [Orange](http://www.orange.com) privacy management service, enabling Orange users to manage access permissions to their personal data and APIs services shared with third party services. More precisely, the "user privacy" service checks if a third party service is authorized to access to Orange users' personal data (like profile or geolocation)/applications (calendar, address book...). A web interface available to Orange users offers a central point to manage access control policies and access history of their personal data (http://privacy.orange.fr). |
| "The Enabling Grids for E-sciencE (EGEE) project is funded by the European Commission and aims to build on recent advances in grid technology and develop a service grid infrastructure which is available to scientists 24 hours-a-day." (http://public.eu-egee.org). EGEE uses the HERASAF XACML implementation for their Authorization Framework (https://twiki.cern.ch/twiki/bin/view/EGEE/AuthorizationFramework). |

## License ##
HERAS-AF XACML Core is available under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0).