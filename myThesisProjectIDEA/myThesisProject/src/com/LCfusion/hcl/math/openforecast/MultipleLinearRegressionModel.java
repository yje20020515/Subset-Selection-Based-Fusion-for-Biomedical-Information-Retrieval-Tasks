package com.LCfusion.hcl.math.openforecast;

//
//  OpenForecast - open source, general-purpose forecasting package.
//  Copyright (C) 2002-2011  Steven R. Gould
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the GNU Lesser General Public
//  License as published by the Free Software Foundation; either
//  version 2.1 of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//  Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public
//  License along with this library; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//




import java.util.Enumeration;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;



/**
 * Implements a multiple variable linear regression model using the variables
 * named in the constructor as the independent variables, or the variables
 * passed into one of the init methods. The cofficients of the regression, as
 * well as the accuracy indicators are determined from the data set passed to
 * init.
 *
 * <p>Once initialized, this model can be applied to another data set using
 * the forecast method to forecast values of the dependent variable based on
 * values of the dependent variable (the one named in the constructor).
 *
 * <p>A multiple variable linear regression model essentially attempts to put
 * a hyperplane through the data points. Mathematically, assuming the
 * independent variables are x<sub>i</sub> and the dependent variable is y,
 * then this hyperplane can be represented as:
 * <pre>y = a<sub>0</sub> + a<sub>1</sub>*x<sub>1</sub> + a<sub>2</sub>*x<sub>2</sub> + a<sub>3</sub>*x<sub>3</sub> + ...</pre>
 * where the a<sub>i</sub> are the coefficients of the regression. The
 * coefficient a<sub>0</sub> is also referred to as the intercept. If all
 * x<sub>i</sub> were zero (theoretically at least), it is the forecast value
 * of the dependentVariable, y.
 * @author Steven R. Gould
 * @since 0.2
 */
public class MultipleLinearRegressionModel extends AbstractForecastingModel
{
    /**
     * The intercept for the linear regression model. Initialized following a
     * call to init.
     */
    private double intercept = 0.0;

    /**
     * An mapping of variable names to coefficients for this multiple variable
     * linear regression model. These are initialized following a call to init.
     */
    private Hashtable<String,Double> coefficient;

    /**
     * A default constructor that constructs a new multiple variable Linear
     * regression model. When this constructor is used, it is assumed that all
     * independent variables in the DataSet or list of coefficients passed to
     * init are to be used in the model. To narrow down and explicitly define
     * what independent variables to use in the model, use the alternate form
     * of the constructor.
     *
     * <p>For a valid model to be constructed, you should call either
     * implementation of init.
     * @see #init(DataSet)
     * @see #init(double,Hashtable)
     */
    public MultipleLinearRegressionModel()
    {
        coefficient = null;
    }

    /**
     * Constructs a new multiple variable linear regression model, using the
     * given array of names as the independent variables to use. For a valid
     * model to be constructed, you should call init and pass in a data set
     * containing a series of data points involving the given independent
     * variables.
     * @param independentVariable an array of names of the independent
     *        variables to use in this model.
     * @see #init(DataSet)
     * @see #init(double,Hashtable)
     */
    public MultipleLinearRegressionModel( String[] independentVariable )
    {
        setIndependentVariables( independentVariable );
    }

    /**
     * Initializes the coefficients to use for this regression model. The
     * coefficients are derived so as to give the best fit hyperplane for the
     * given data set.
     *
     * <p>Additionally, the accuracy indicators are calculated based on this
     * data set.
     * @param dataSet the set of observations to use to derive the regression
     *        coefficients for this model.
     */
    public void init( DataSet dataSet )
    {
        String varNames[] = dataSet.getIndependentVariables();

        // If no coefficients have been defined for this model,
        //  use all that exist in this data set
        if ( coefficient == null )
            setIndependentVariables( varNames );

        int n = varNames.length;
        double a[][] = new double[n+1][n+2];

        // Iterate through dataSet
        Iterator<DataPoint> it = dataSet.iterator();
        while ( it.hasNext() )
            {
                // Get next data point
                DataPoint dp = it.next();

                // For each row in the matrix, a
                for ( int row=0; row<n+1; row++ )
                    {
                        double rowMult = 1.0;
                        if ( row != 0 )
                            {
                                // Get value of independent variable, row
                                String rowVarName = varNames[row-1];
                                rowMult = dp.getIndependentValue(rowVarName);
                            }

                        // For each column in the matrix, a
                        for ( int col=0; col<n+2; col++ )
                            {
                                double colMult = 1.0;
                                if ( col == n+1 )
                                    {
                                        // Special case, for last column
                                        //  use value of dependent variable
                                        colMult = dp.getDependentValue();
                                    }
                                else if ( col > 0 )
                                    {
                                        // Get value of independent variable, col
                                        String colVarName = varNames[col-1];
                                        colMult = dp.getIndependentValue(colVarName);
                                    }

                                a[row][col] += rowMult * colMult;
                            }
                    }
            }

        // Solve equations to derive coefficients
        double coeff[] = Utils.GaussElimination( a );

        // Assign coefficients to independent variables
        intercept = coeff[0];
        for ( int i=1; i<n+1; i++ )
            coefficient.put( varNames[i-1], new Double(coeff[i]) );

        // Calculate the accuracy indicators
        calculateAccuracyIndicators( dataSet );
    }

    /**
     * Initializes the coefficients to use for this regression model with the
     * given values. The coefficients Hashtable must contain a single entry
     * for each independent variable. That entry should be accessible via the
     * exact same name (case sensitive) as the independent variable, and
     * should contain the intended coefficient value (as a subclass of
     * Number).
     *
     * <p>In addition to the coefficients, you must also specify the
     * constant term - or the "intercept" - to use in the regression model.
     *
     * <p>Note that since this method does not require a data set, it is
     * not possible to calculate the values of the accuracy indicators.
     * Therefore these are all set to their worse-case values.
     * @param intercept the constant term - or the "intercept" - to use in
     *        the regression model.
     * @param coefficients the coefficients to be assigned for use in this
     *        regression model.
     * @throws ModelNotInitializedException if a coefficient does not exist
     *        in the given Hashtable of coefficients for one or more of the
     *        independent variables used when constructing this model.
     * @throws ClassCastException if the "value" associated with one of the
     *        coefficients cannot be cast to a Number.
     */
    public void init( double intercept, Hashtable<String,Double> coefficients )
    {
        // If no coefficients have been defined for this model,
        //  use all that exist in the list of coefficients
        if ( coefficient == null )
            {
                // Iterate through the set of keys, building a String[]
                Enumeration<String> keysEnum = coefficients.keys();
                String[] keys = new String[ coefficients.size() ];
                int k = 0;
                while ( keysEnum.hasMoreElements() )
                    keys[k++] = keysEnum.nextElement();

                setIndependentVariables( keys );
            }

        this.intercept = intercept;

        // Iterate through the variable names in this.coefficient
        Iterator<String> it = coefficient.keySet().iterator();
        while ( it.hasNext() )
            {
                // Get variable name
                String name = it.next();

                // Look up variable in coefficients Hashtable
                double coeff = ((Number)coefficients.get(name)).doubleValue();

                // Assign value back into this.coefficient
                coefficient.put( name, new Double(coeff) );
            }

        // Indicate that the model has been initialized
        initialized = true;

        // Note that accuracy indicators by default are initialized to
        //  their worst possible values, so no need to update them here
    }

    /**
     * Returns the intercept that will be used by the current model. This
     * could be a user-defined value or internally calculated depending on
     * which of the init methods was used to initialize the model.
     * @return the intercept that will be used by this model.
     * @throws ModelNotInitializedException if this method is called before
     *         the model has been initialized with a call to init.
     */
    public double getIntercept()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();

        return intercept;
    }

    /**
     * Returns a Hashtable containing the coefficients that will be used by
     * the current model. These could be user-defined coefficients or
     * internally calculated coefficients depending on which of the init
     * methods were used to initialize the model.
     *
     * <p>Note that modifying the coefficients in the Hashtable returned from
     * this method will not modify the coefficients used by the model. If you
     * want to modify the coefficients used in the model, then use the
     * {@link #init(double,Hashtable)} method to re-initialize the model.
     * @return a Hashtable containing the coefficients that will be used by
     * this model.
     * @throws ModelNotInitializedException if this method is called before
     *         the model has been initialized with a call to init.
     */
    public Hashtable<String,Double> getCoefficients()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();

        return new Hashtable<String,Double>( coefficient );
    }

    /**
     * Returns the number of predictors used by the underlying model.
     * @return the number of predictors used by the underlying model.
     */
    public int getNumberOfPredictors()
    {
        return coefficient.size();
    }

    /**
     * Using the current model parameters (initialized in init), apply the
     * forecast model to the given data point. The data point must have valid
     * values for the independent variables. Upon return, the value of the
     * dependent variable will be updated with the forecast value computed for
     * that data point.
     * @param dataPoint the data point for which a forecast value (for the
     *        dependent variable) is required.
     * @return the forecast value of the dependent variable for the given data
     *         point.
     * @throws ModelNotInitializedException if forecast is called before the
     *         model has been initialized with a call to init.
     */
    public double forecast( DataPoint dataPoint )
    {
        if ( !initialized )
            throw new ModelNotInitializedException();

        double forecastValue = intercept;

        Iterator< Map.Entry<String,Double> > it = coefficient.entrySet().iterator();
        while ( it.hasNext() )
            {
                Map.Entry<String,Double> entry = it.next();

                // Get value of independent variable
                double x = dataPoint.getIndependentValue( (String)entry.getKey() );

                // Get coefficient for this variable
                double coeff = ((Double)entry.getValue()).doubleValue();
                forecastValue += coeff * x;
            }

        dataPoint.setDependentValue( forecastValue );

        return forecastValue;
    }

    /**
     * A helper function that initializes the set of independent variables used
     * in this model to the given array of independent variable names.
     * @param independentVariable an array of independent variables to use in
     *        this MultipleLinearRegressionModel.
     */
    private void setIndependentVariables(String[] independentVariable)
    {
        // Create a new hashtable of just the right size
        coefficient = new Hashtable<String,Double>( independentVariable.length );

        // Add each independent variable with an initial coefficient of 0.0
        for ( int v=0; v<independentVariable.length; v++ )
            coefficient.put( independentVariable[v], new Double(0.0) );
    }

    /**
     * Returns a short name for this type of forecasting model. A more detailed
     * explanation is provided by the toString method.
     * @return a short string describing this type of forecasting model.
     */
    public String getForecastType()
    {
        return "Multiple variable linear regression";
    }

    /**
     * Returns a detailed description of this forcasting model, including the
     * intercept and slope. A shortened version of this is provided by the
     * getForecastType method.
     * @return a description of this forecasting model.
     */
    public String toString()
    {
        String desc = "Multiple variable linear regression model with the following equation:\n"
            +"  y="+intercept;

        Set< Map.Entry<String,Double> > coeffSet = coefficient.entrySet();
        Iterator< Map.Entry<String,Double> > it = coeffSet.iterator();
        while ( it.hasNext() )
            {
                Map.Entry<String,Double> entry = it.next();
                double coeff = ((Double)entry.getValue()).doubleValue();
                if ( coeff < -TOLERANCE )
                    desc += coeff + "*" + entry.getKey();
                else if ( coeff > TOLERANCE )
                    desc += "+" + coeff + "*" + entry.getKey();
                // else coeff == 0.0
            }

        return desc;
    }
}
// Local variables:
// tab-width: 4
// End:
