@Grab( 'com.miglayout:miglayout:3.7.4' )
import net.miginfocom.swing.MigLayout;
import groovy.swing.SwingBuilder
import groovy.swing.factory.LayoutFactory
import static javax.swing.JFrame.*
import groovy.text.SimpleTemplateEngine

def validateURL = { url ->
    assert url.text?.size() > 0
}

new SwingBuilder().with {
    registerFactory( 'migLayout', new LayoutFactory( MigLayout ) )
    build {
        frame( title:'Plugin Creation Tool',
               pack:true,
               show:true,
               defaultCloseOperation:EXIT_ON_CLOSE,
               layout: new MigLayout( layoutConstraints:'ins dialog', columnConstraints:'[][]', rowConstraints:'' ) ) {
            label 'Plugin Name'
            textField id:'tfPluginName', columns:40, constraints:'wrap'

            label 'Plugin URL'
            textField id:'tfPluginUrl', columns:40, constraints:'wrap'

            label 'Author URL'
            textField id:'tfAuthorUrl', columns:40, constraints:'wrap'

            label 'Author Name'
            textField id:'tfAuthorName', columns:40, constraints:'wrap'

            label 'Version'
            textField id:'tfVersion', columns:40, constraints:'wrap'

            label 'Plugin Desc'
            scrollPane( constraints:'wrap' ) {
                textArea id:'taPluginDesc', columns:40, rows:5
            }

            label 'Example Usage'
            scrollPane( constraints:'wrap' ) {
                textArea id:'taPluginExample', columns:40, rows:15
            }
            
            button 'Generate!', actionPerformed:{ e ->
                validateURL( new URL( tfPluginUrl.text ) )
                validateURL( new URL( tfAuthorUrl.text ) )
                def output = new File( "../plugins/gradle-${tfPluginName.text.replaceAll( ' ', '' ).toLowerCase()}-plugin.html" )
                assert !output.exists()
                assert tfPluginName.text.size() > 0
                assert tfAuthorName.text.size() > 0
                assert tfVersion.text.size() > 0
                assert taPluginDesc.text.size() > 0
                assert taPluginExample.text.size() > 0
                output << new SimpleTemplateEngine().createTemplate( new File( 'template.html' ).text )
                                                    .make( title: tfPluginName.text,
                                                           description: taPluginDesc.text,
                                                           url: tfPluginUrl.text,
                                                           authorUrl: tfAuthorUrl.text,
                                                           author: tfAuthorName.text,
                                                           version: tfVersion.text,
                                                           usage: taPluginExample.text ).toString()
            }
        }
    } 
}