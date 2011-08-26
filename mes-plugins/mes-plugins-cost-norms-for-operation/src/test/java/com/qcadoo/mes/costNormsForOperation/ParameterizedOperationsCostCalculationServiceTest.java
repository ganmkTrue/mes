package com.qcadoo.mes.costNormsForOperation;

/*import static com.qcadoo.mes.costNormsForOperation.constants.OperationsCostCalculationConstants.HOURLY;
 import static com.qcadoo.mes.costNormsForOperation.constants.OperationsCostCalculationConstants.PIECEWORK;
 import static java.math.BigDecimal.valueOf;
 import static org.junit.Assert.assertEquals;
 import static org.mockito.Mockito.mock;
 import static org.mockito.Mockito.when;
 import static org.springframework.test.util.ReflectionTestUtils.setField;

 import java.math.BigDecimal;
 import java.util.Arrays;
 import java.util.Collection;
 import java.util.Iterator;
 import java.util.Map;

 import org.junit.Before;
 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.junit.runners.Parameterized;
 import org.junit.runners.Parameterized.Parameters;
 import org.springframework.beans.factory.annotation.Autowired;

 import com.qcadoo.mes.costNormsForOperation.constants.OperationsCostCalculationConstants;
 import com.qcadoo.mes.productionScheduling.OrderRealizationTimeService;
 import com.qcadoo.model.api.Entity;
 import com.qcadoo.model.api.EntityList;
 import com.qcadoo.model.api.EntityTree;
 import com.qcadoo.model.api.EntityTreeNode;

 @RunWith(Parameterized.class)
 public class ParameterizedOperationsCostCalculationServiceTest {

 @Autowired
 private OrderRealizationTimeService orderRealizationTimeService;

 private OperationsCostCalculationService operationCostCalculationService;

 private Entity source;

 private EntityTreeNode operationComponent;

 private BigDecimal validateLaborHourlyCost, validateMachineHourlyCost, validatePieceworkCost, validateOrderQuantity,
 validateExpectedMachine, validateExpectedLabor, validateNumberOfOperations, validateExpectedPieceworkCost,
 validationOutputQuantity;

 int realizationTime, expectedrealizationTime;

 private OperationsCostCalculationConstants validateMode;

 private boolean validateIncludeTPZs;

 @Parameters
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 // mode, laborHourly, machineHourly, piecework, numOfOps, includeTPZs, order qtty, expectedMachine, expectedLabor
 // pieceWorkCost, validationOutputQuantity, expectedrealizationTime
 { HOURLY, valueOf(20), valueOf(10), valueOf(35), valueOf(1), false, valueOf(1), valueOf(22), valueOf(20),
 valueOf(0), valueOf(5), 20 },
 { PIECEWORK, valueOf(20), valueOf(10), valueOf(35), valueOf(1), false, valueOf(1), valueOf(0), valueOf(0),
 valueOf(175), valueOf(5), 0 } });
 }

 public ParameterizedOperationsCostCalculationServiceTest(OperationsCostCalculationConstants mode, BigDecimal laborHourly,
 BigDecimal machineHourly, BigDecimal pieceWork, BigDecimal numOfOperations, boolean includeTPZs,
 BigDecimal orderQuantity, BigDecimal expectedMachine, BigDecimal expectedLabor,
 BigDecimal validateExpectedPieceworkCost, BigDecimal validationOutputQuantity, int expectedrealizationTime) {
 this.validateIncludeTPZs = includeTPZs;
 this.validateLaborHourlyCost = laborHourly;
 this.validateMachineHourlyCost = machineHourly;
 this.validateNumberOfOperations = numOfOperations;
 this.validatePieceworkCost = pieceWork;
 this.validateMode = mode;
 this.validateOrderQuantity = orderQuantity;
 this.validateExpectedLabor = expectedLabor;
 this.validateExpectedMachine = expectedMachine;
 this.validateExpectedPieceworkCost = validateExpectedPieceworkCost;
 this.validationOutputQuantity = validationOutputQuantity;
 this.expectedrealizationTime = expectedrealizationTime;
 }

 @Before
 public void init() {

 source = mock(Entity.class);

 EntityList outputProducts = mock(EntityList.class);
 Entity outputProduct = mock(Entity.class);
 Iterator<Entity> outputProductsIterator = mock(Iterator.class);
 EntityTree operationComponents = mock(EntityTree.class);

 // === OPERATION COMPONENT ===
 operationComponent = mock(EntityTreeNode.class);

 when(source.getTreeField("operationComponents")).thenReturn(operationComponents);

 Iterator<Entity> operationComponentsIterator = mock(Iterator.class);
 when(operationComponentsIterator.hasNext()).thenReturn(true, false);
 when(operationComponentsIterator.next()).thenReturn(operationComponent);
 when(operationComponents.iterator()).thenReturn(operationComponentsIterator);
 when(operationComponents.size()).thenReturn(1);
 when(operationComponents.getRoot()).thenReturn(operationComponent);

 when(operationComponent.getField("laborHourlyCost")).thenReturn(validateLaborHourlyCost);
 when(operationComponent.getField("machineHourlyCost")).thenReturn(validateMachineHourlyCost);
 when(operationComponent.getField("pieceworkCost")).thenReturn(validatePieceworkCost);
 when(operationComponent.getField("numberOfOperations")).thenReturn(validateNumberOfOperations);

 orderRealizationTimeService = mock(OrderRealizationTimeService.class);

 when(
 orderRealizationTimeService.estimateRealizationTimeForOperation(operationComponent, validateOrderQuantity,
 validateIncludeTPZs)).thenReturn(realizationTime);

 operationCostCalculationService = new OperationsCostCalculationServiceImpl();
 setField(operationCostCalculationService, "orderRealizationTimeService", orderRealizationTimeService);

 when(outputProductsIterator.hasNext()).thenReturn(true, false);
 when(outputProductsIterator.next()).thenReturn(outputProduct);
 when(outputProducts.iterator()).thenReturn(outputProductsIterator);

 when(operationComponent.getHasManyField("operationProductOutComponents")).thenReturn(outputProducts);
 when(outputProduct.getField("quantity")).thenReturn(validationOutputQuantity);

 }

 @Test
 public void shouldReturnCorrectValuesUsingTechnology() throws Exception {
 // when
 Map<String, BigDecimal> result = operationCostCalculationService.calculateOperationsCost(source, validateMode,
 validateIncludeTPZs, validateOrderQuantity);
 // then
 assertEquals(validateExpectedLabor, result.get("totalLaborHourlyCost"));
 assertEquals(validateExpectedMachine, result.get("totalMachineHourlyCost"));
 assertEquals(validateExpectedPieceworkCost, result.get("totalPieceWorkCost"));
 }
 }*/