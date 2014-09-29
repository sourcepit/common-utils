/*
 * Copyright 2014 Bernd Vogt and others.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sourcepit.common.utils.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class XmlUtils
{
   private XmlUtils()
   {
      super();
   }

   public static String getEncoding(InputStream inputStream)
   {
      return SAXEncodingDetector.parse(inputStream);
   }

   public static Document newDocument()
   {
      final DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
      final DocumentBuilder docBuilder;
      try
      {
         docBuilder = dbfac.newDocumentBuilder();
      }
      catch (ParserConfigurationException e)
      {
         throw new IllegalStateException(e);
      }
      final Document document = docBuilder.newDocument();
      document.setXmlStandalone(true);
      return document;
   }

   public static Document readXml(InputStream inputStream)
   {
      try
      {
         Document document = newDocumentBuilder().parse(inputStream);
         document.setXmlStandalone(true);
         return document;
      }
      catch (IOException e)
      {
         throw new IllegalArgumentException(e);
      }
      catch (SAXException e)
      {
         throw new IllegalArgumentException(e);
      }
      catch (ParserConfigurationException e)
      {
         throw new IllegalStateException(e);
      }
   }

   public static Document readXml(File xmlFile) throws IllegalArgumentException
   {
      try
      {
         Document document = newDocumentBuilder().parse(xmlFile);
         document.setXmlStandalone(true);
         return document;
      }
      catch (IOException e)
      {
         throw new IllegalArgumentException(e);
      }
      catch (SAXException e)
      {
         throw new IllegalArgumentException(e);
      }
      catch (ParserConfigurationException e)
      {
         throw new IllegalStateException(e);
      }
   }

   private static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException
   {
      final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setIgnoringComments(false);
      factory.setIgnoringElementContentWhitespace(true);
      return factory.newDocumentBuilder();
   }

   public static void writeXml(Document doc, OutputStream outputStream)
   {
      try
      {
         // Prepare the DOM document for writing
         Source source = new DOMSource(doc);

         // Prepare the output file
         Result result = new StreamResult(outputStream);

         // Write the DOM document to the file
         Transformer xformer = TransformerFactory.newInstance().newTransformer();
         try
         {
            xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
         }
         catch (IllegalArgumentException e)
         { // ignore
         }

         try
         {
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
         }
         catch (IllegalArgumentException e)
         { // ignore
         }

         xformer.transform(source, result);
      }
      catch (TransformerConfigurationException e)
      {
         throw new IllegalStateException(e);
      }
      catch (TransformerException e)
      {
         throw new IllegalStateException(e);
      }
   }

   // This method writes a DOM document to a file
   public static void writeXml(Document doc, File file)
   {
      try
      {
         // Prepare the DOM document for writing
         Source source = new DOMSource(doc);

         // Prepare the output file
         Result result = new StreamResult(file);

         // Write the DOM document to the file
         Transformer xformer = TransformerFactory.newInstance().newTransformer();

         try
         {
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
         }
         catch (IllegalArgumentException e)
         { // ignore
         }

         xformer.transform(source, result);
      }
      catch (TransformerConfigurationException e)
      {
         throw new IllegalStateException(e);
      }
      catch (TransformerException e)
      {
         throw new IllegalStateException(e);
      }
   }

   public static Iterable<Node> queryNodes(Document document, String xPath)
   {
      return toIterable(queryNodeList(document, xPath));
   }

   public static Node queryNode(Document document, String xPath)
   {
      try
      {
         XPathExpression expr = XPathFactory.newInstance().newXPath().compile(xPath);
         return (Node) expr.evaluate(document, XPathConstants.NODE);
      }
      catch (XPathExpressionException e)
      {
         throw new IllegalArgumentException(e);
      }
   }

   public static NodeList queryNodeList(Document document, String xPath)
   {
      try
      {
         XPathExpression expr = XPathFactory.newInstance().newXPath().compile(xPath);
         return (NodeList) expr.evaluate(document, XPathConstants.NODESET);
      }
      catch (XPathExpressionException e)
      {
         throw new IllegalArgumentException(e);
      }
   }

   public static Iterable<Node> toIterable(NodeList nodeList)
   {
      return nodeList == null ? DomIterable.EMPTY_ITERABLE : new DomIterable(nodeList);
   }

   public static String queryText(Document document, String xPath)
   {
      try
      {
         XPathExpression expr = XPathFactory.newInstance().newXPath().compile(xPath);
         return (String) expr.evaluate(document, XPathConstants.STRING);
      }
      catch (XPathExpressionException e)
      {
         throw new IllegalArgumentException(e);
      }
   }

   public static class DomIterable implements Iterable<Node>
   {
      private static final DomIterable EMPTY_ITERABLE = new DomIterable(new NodeList()
      {
         public Node item(int index)
         {
            return null;
         }

         public int getLength()
         {
            return 0;
         }
      });

      private final NodeList nodeList;

      public DomIterable(NodeList nodeList)
      {
         this.nodeList = nodeList;
      }

      public static Iterable<Node> newIterable(Element element, String tagName)
      {
         return new DomIterable(element.getElementsByTagName(tagName));
      }

      public static Iterable<Node> newIterable(Document document, String tagName)
      {
         return new DomIterable(document.getElementsByTagName(tagName));
      }

      public static Iterable<Node> newIterable(Node node, String tagName)
      {
         if (node instanceof Element)
         {
            return newIterable((Element) node, tagName);
         }
         else if (node instanceof Document)
         {
            return newIterable((Document) node, tagName);
         }
         return EMPTY_ITERABLE;
      }

      public Iterator<Node> iterator()
      {
         return new NodeIterator(nodeList);
      }
   }

   private static class NodeIterator implements Iterator<Node>, Iterable<Node>
   {
      private final NodeList nodeList;
      private int i = 0;

      public NodeIterator(NodeList nodeList)
      {
         this.nodeList = nodeList;
      }

      public boolean hasNext()
      {
         return nodeList.getLength() > i;
      }

      public Node next()
      {
         return nodeList.item(i++);
      }

      public void remove()
      {
         throw new UnsupportedOperationException();
      }

      public Iterator<Node> iterator()
      {
         return this;
      }
   }
}
