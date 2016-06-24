﻿-- rename fields in settings
-- last touched 2.06.2016 by pako

UPDATE materialflowresources_documentpositionparametersitem SET name = 'productionDate' WHERE name = 'productiondate';
UPDATE materialflowresources_documentpositionparametersitem SET name = 'expirationDate' WHERE name = 'expirationdate';
-- resource stock view
-- last touched 30.05.2016 by kama

CREATE TABLE materialflowresources_resourcestock
(
  id bigint NOT NULL,
  location_id bigint,
  product_id bigint,
  quantity numeric(12,5),
  reservedquantity numeric(12,5),
  availablequantity numeric(12,5),
  CONSTRAINT materialflowresources_resourcestock_pkey PRIMARY KEY (id),
  CONSTRAINT resourcestock_product_fkey FOREIGN KEY (product_id)
      REFERENCES basic_product (id) DEFERRABLE,
  CONSTRAINT resourcestock_location_fkey FOREIGN KEY (location_id)
      REFERENCES materialflow_location (id) DEFERRABLE
);

CREATE OR REPLACE VIEW materialflowresources_orderedquantitystock AS
    SELECT COALESCE(SUM(orderedproduct.orderedquantity), 0::numeric) AS orderedquantity,
    resource.id AS resource_id
    FROM materialflowresources_resourcestock resource
    JOIN deliveries_orderedproduct orderedproduct ON (orderedproduct.product_id = resource.product_id)
    JOIN deliveries_delivery delivery ON (orderedproduct.delivery_id = delivery.id AND delivery.active = true
        AND delivery.location_id = resource.location_id
        AND (delivery.state::text = ANY (ARRAY['01draft'::character varying::text, '02prepared'::character varying::text, '03duringCorrection'::character varying::text, '05approved'::character varying::text])))
    GROUP BY resource.id;

CREATE OR REPLACE VIEW materialflowresources_resourcestockdto_internal AS
    SELECT row_number() OVER () AS id, resource.location_id, resource.product_id::integer, resource.quantity AS quantity,
    COALESCE(orderedquantity.orderedquantity, 0::numeric) AS orderedquantity,
    (SELECT SUM(warehouseminimalstate_warehouseminimumstate.minimumstate) AS sum
        FROM warehouseminimalstate_warehouseminimumstate WHERE warehouseminimalstate_warehouseminimumstate.product_id = resource.product_id
            AND warehouseminimalstate_warehouseminimumstate.location_id = resource.location_id) AS minimumstate,
    reservedQuantity, availableQuantity
    FROM materialflowresources_resourcestock resource
    LEFT JOIN materialflowresources_orderedquantitystock orderedquantity ON (orderedquantity.resource_id = resource.id)
    GROUP BY resource.location_id, resource.product_id, orderedquantity.orderedquantity, reservedQuantity, availableQuantity, quantity;

CREATE OR REPLACE VIEW materialflowresources_resourcestockdto AS
    SELECT internal.*, location.number AS locationNumber, location.name AS locationName, product.number AS productNumber,
    product.name AS productName, product.unit AS productUnit
    FROM materialflowresources_resourcestockdto_internal internal
    JOIN materialflow_location location ON (location.id = internal.location_id)
    JOIN basic_product product ON (product.id = internal.product_id);


-- end

-- new delivered product fields
ď»ż-- last touched 9.06.2016 by pako

ALTER TABLE deliveries_deliveredproduct ADD COLUMN palletnumber_id bigint;
ALTER TABLE deliveries_deliveredproduct ADD COLUMN pallettype character varying(255);
ALTER TABLE deliveries_deliveredproduct ADD COLUMN storagelocation_id bigint;
ALTER TABLE deliveries_deliveredproduct ADD COLUMN additionalcode_id bigint;

ALTER TABLE deliveries_deliveredproduct
  ADD CONSTRAINT deliveredproduct_additionalcode_fkey FOREIGN KEY (additionalcode_id)
      REFERENCES basic_additionalcode (id) DEFERRABLE;
ALTER TABLE deliveries_deliveredproduct
  ADD CONSTRAINT deliveredproduct_palletnumber_fkey FOREIGN KEY (palletnumber_id)
      REFERENCES basic_palletnumber (id) DEFERRABLE;
ALTER TABLE deliveries_deliveredproduct
  ADD CONSTRAINT deliveredproduct_storagelocation_fkey FOREIGN KEY (storagelocation_id)
      REFERENCES materialflowresources_storagelocation (id) DEFERRABLE;

-- end

-- deliveries changes
-- last touched 20.06.2016 by pako

ALTER TABLE deliveries_deliveredproduct ADD COLUMN additionalquantity numeric(12,5);
ALTER TABLE deliveries_deliveredproduct ADD COLUMN conversion numeric(12,5);
ALTER TABLE deliveries_deliveredproduct ADD COLUMN iswaste boolean;
ALTER TABLE deliveries_deliveredproduct ADD COLUMN additionalunit character varying(255);

CREATE TABLE deliveries_deliveredproductmulti
(
  id bigint NOT NULL,
  delivery_id bigint,
  palletnumber_id bigint,
  pallettype character varying(255),
  storagelocation_id bigint,
  createdate timestamp without time zone,
  updatedate timestamp without time zone,
  createuser character varying(255),
  updateuser character varying(255),
  CONSTRAINT deliveries_deliveredproductmulti_pkey PRIMARY KEY (id),
  CONSTRAINT deliveredproductmulti_delivery_fkey FOREIGN KEY (delivery_id)
      REFERENCES deliveries_delivery (id) DEFERRABLE,
  CONSTRAINT deliveredproductmulti_palletnumber_fkey FOREIGN KEY (palletnumber_id)
      REFERENCES basic_palletnumber (id) DEFERRABLE,
  CONSTRAINT deliveredproductmulti_storagelocation_fkey FOREIGN KEY (storagelocation_id)
      REFERENCES materialflowresources_storagelocation (id) DEFERRABLE
);

CREATE TABLE deliveries_deliveredproductmultiposition
(
  id bigint NOT NULL,
  deliveredproductmulti_id bigint,
  product_id bigint,
  quantity numeric(12,5),
  additionalquantity numeric(12,5),
  conversion numeric(12,5),
  iswaste boolean,
  expirationdate date,
  unit character varying(255),
  additionalunit character varying(255),
  additionalcode_id bigint,
  CONSTRAINT deliveries_deliveredproductmultiposition_pkey PRIMARY KEY (id),
  CONSTRAINT deliveredproductmp_deliveredproductmulti_fkey FOREIGN KEY (deliveredproductmulti_id)
      REFERENCES deliveries_deliveredproductmulti (id) DEFERRABLE,
  CONSTRAINT deliveredproductmp_product_fkey FOREIGN KEY (product_id)
      REFERENCES basic_product (id) DEFERRABLE,
  CONSTRAINT deliveredproductmp_additionalcode_fkey FOREIGN KEY (additionalcode_id)
      REFERENCES basic_additionalcode (id) DEFERRABLE
);

-- end
