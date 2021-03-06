<?xml version="1.0"?>
<article>

    <section>
        <title>Beställda produkter</title>
    <#list prescriptionItems as item>
        <variablelist>
            <varlistentry>
                <term>&nbsp;</term>
                <listitem>&nbsp;</listitem>
            </varlistentry>
            <varlistentry>
                <term>Produktgrupp:</term>
                <listitem>${item.article.productArea?capitalize}</listitem>
            </varlistentry>
            <varlistentry>
                <term></term>
                <listitem>${item.article.articleName}</listitem>
            </varlistentry>
            <varlistentry>
                <term>Artikelnr.:</term>
                <listitem>${item.article.articleNo}</listitem>
            </varlistentry>
        </variablelist>
    </#list>
    </section>

    <section>
        <title>Leveransinformation</title>
    <#list deliveryChoices as choice>
        <variablelist>
            <varlistentry>
                <term>&nbsp;</term>
                <listitem>&nbsp;</listitem>
            </varlistentry>
            <#if choice.deliveryMethod.name() == 'UTLÄMNINGSSTÄLLE'>
                <varlistentry>
                    <term></term>
                    <listitem>Utlämningsställe:</listitem>
                </varlistentry>
                <varlistentry>
                    <term>${choice.deliveryPoint.deliveryPointName}</term>
                    <listitem>&nbsp;</listitem>
                </varlistentry>
                <varlistentry>
                    <term>${choice.deliveryPoint.deliveryPointAddress}</term>
                    <listitem>&nbsp;</listitem>
                </varlistentry>
                <varlistentry>
                    <term>${choice.deliveryPoint.deliveryPointPostalCode} ${choice.deliveryPoint.deliveryPointCity}</term>
                    <listitem>&nbsp;</listitem>
                </varlistentry>
            <#else >
                <varlistentry>
                    <term></term>
                    <listitem>Hemleverans:</listitem>
                </varlistentry>
                <varlistentry>
                    <term>${choice.homeDeliveryAddress.receiver}</term>
                    <listitem>&nbsp;</listitem>
                </varlistentry>
                <varlistentry>
                    <term>${choice.homeDeliveryAddress.street}</term>
                    <listitem>&nbsp;</listitem>
                </varlistentry>
                <varlistentry>
                    <term>${choice.homeDeliveryAddress.postalCode} ${choice.homeDeliveryAddress.city}</term>
                    <listitem>&nbsp;</listitem>
                </varlistentry>
            </#if>
        </variablelist>

        <#if choice.deliveryNotificationMethod??>
            <variablelist>
                <varlistentry>
                    <term>&nbsp;</term>
                    <listitem>&nbsp;</listitem>
                </varlistentry>
            </variablelist>

            <variablelist>
                <varlistentry>
                    <term></term>
                    <listitem>${choice.deliveryNotificationMethod.value.value()?capitalize}-avisering:</listitem>
                </varlistentry>
                <#if choice.deliveryNotificationMethod.value.name() == 'BREV'>
                    <varlistentry>
                        <term>${choice.homeDeliveryAddress.receiver}</term>
                        <listitem>&nbsp;</listitem>
                    </varlistentry>
                    <varlistentry>
                        <term>${choice.homeDeliveryAddress.street}</term>
                        <listitem>&nbsp;</listitem>
                    </varlistentry>
                    <varlistentry>
                        <term>${choice.homeDeliveryAddress.postalCode} ${choice.homeDeliveryAddress.city}</term>
                        <listitem>&nbsp;</listitem>
                    </varlistentry>
                <#else>
                    <varlistentry>
                        <term>${choice.deliveryNotificationReceiver}</term>
                        <listitem>&nbsp;</listitem>
                    </varlistentry>
                </#if>
            </variablelist>
            <variablelist>
                <varlistentry>
                    <term>&nbsp;</term>
                    <listitem>&nbsp;</listitem>
                </varlistentry>
            </variablelist>
        </#if>
    </#list>
    </section>

</article>