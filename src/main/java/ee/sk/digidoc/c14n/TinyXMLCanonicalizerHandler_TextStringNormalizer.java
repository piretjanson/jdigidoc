package ee.sk.digidoc.c14n;
import org.apache.log4j.Logger;
import ee.sk.digidoc.c14n.EntityParser_Entity;
import ee.sk.digidoc.c14n.EntityParser_Handler;
import ee.sk.digidoc.c14n.TinyXMLCanonicalizerHandler_TextStringNormalizer_EntityHelper;
import ee.sk.digidoc.c14n.common.Convert;
import ee.sk.digidoc.c14n.common.Helper;
import ee.sk.digidoc.c14n.common.StringImplementation;

class TinyXMLCanonicalizerHandler_TextStringNormalizer implements EntityParser_Handler
{
    public boolean IsAttribute;
    private static Logger m_logger = Logger.getLogger(TinyXMLCanonicalizerHandler_TextStringNormalizer.class);

    public TinyXMLCanonicalizerHandler_TextStringNormalizer()
    {
    }


    public String ResolveEntity(EntityParser_Entity e)
    {
    	if(m_logger.isDebugEnabled())
        	m_logger.debug("Resolve entity orig: " + e.get_OriginalString() + " text: " + e.get_Text() + " attr: " + this.IsAttribute);
        if (e.get_IsNumeric())
        {

            if (!this.IsAttribute)
            {

                if ((e.get_IntegerValue() == 10))
                {
                    return "\n";
                }

            }


            if ((e.get_IntegerValue() == 32))
            {
                return " ";
            }


            if (Helper.IsVisibleChar(e.get_IntegerValue()))
            {
                return Convert.ToString(((char)e.get_IntegerValue()));
            }

            return "&#x"+ e.get_HexValue()+ ";";
        }

        // VS: replace &apos; -> ' also in element body
        //if (this.IsAttribute) {
            if (e.get_Text().equals("apos"))
            {
                return "\'";
            }
        //}

        return e.get_OriginalString();
    }

    public String ResolveText(String e)
    {
        TinyXMLCanonicalizerHandler_TextStringNormalizer_EntityHelper h;

        h = new TinyXMLCanonicalizerHandler_TextStringNormalizer_EntityHelper(e);
        h.set_Item("&", "&amp;");
        h.set_Item("\r", "&#xD;");

        if (this.IsAttribute)
        {
            h.set_Item("\"", "&quot;");
            h.set_Item("\t", "&#x9;");
            h.set_Item("\n", "&#xA;");
        }
        else
        {
            h.set_Item("<", "&lt;");
            h.set_Item(">", "&gt;");
        }
        if(m_logger.isDebugEnabled())
        	m_logger.debug("Resolve: \n" + e + "\nTO:\n" + h.Text);
        return h.Text;
    }

    public static String StaticResolveTextCData(String e)
    {
        TinyXMLCanonicalizerHandler_TextStringNormalizer_EntityHelper h;

        h = new TinyXMLCanonicalizerHandler_TextStringNormalizer_EntityHelper(e);
        h.set_Item("&", "&amp;");
        h.set_Item("<", "&lt;");
        h.set_Item(">", "&gt;");
        h.set_Item("\r", "&#xD;");
        if(m_logger.isDebugEnabled())
        	m_logger.debug("Normalize: \n" + e + "\nTO:\n" + h.Text);
        return h.Text;
    }

}
