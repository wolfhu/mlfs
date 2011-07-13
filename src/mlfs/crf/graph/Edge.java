/*
 * Edge.java 
 * 
 * Author : 罗磊，luoleicn@gmail.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Last Update:Jul 12, 2011
 * 
 */
package mlfs.crf.graph;

import java.util.List;

import mlfs.crf.Features;
import mlfs.crf.model.CRFEvent;

/**
 * 连接两个Node的边
 */
public class Edge {

	/** 左节点. */
	public final Node m_lnode;
	
	/** 右节点. */
	public final Node m_rnode;
	
	/** 边的概率. */
	private double m_bigramProb;
	
	/** 满足的所有bigram特征. */
	private List<Integer> m_features;
	
	/**
	 * Instantiates a new edge.
	 *
	 * @param l 左节点(前一个节点) 
	 * @param r 右节点(后一个节点)
	 */
	public Edge(Node l, Node r)
	{
		this.m_lnode = l;
		this.m_rnode = r;
	}
	
	/**
	 * 计算这个边的概率，log形式
	 *
	 * @param parameters crf模型参数
	 */
	public void calcLogProbs(double[] parameters, int tagSize)
	{
		m_bigramProb = 0.0;
		for (int f : m_features)
		{
			m_bigramProb += parameters[f*tagSize + m_rnode.m_y];
		}
	}
	
	/**
	 * 计算这条边满足的特征
	 */
	public void calFeature(CRFEvent e, int idx, Features handler)
	{
		if (idx == 0)
			m_features = null;
		else
			m_features = handler.getBigramFeat(e, m_lnode.m_y, idx);
	}
	
	/**
	 * 计算这条边的模型期望.
	 *
	 * @param expectation 模型期望
	 * @param Z 归一化因子
	 */
	public void calcModelExpectation(double[][] expectation, double Z)
	{
		double prob = m_lnode.getAlpha() + m_bigramProb + m_rnode.getBeta() - Z;
		for (int f : m_features)
		{
			expectation[f][m_rnode.m_y] += Math.exp(prob);
		}
	}
	
	/**
	 * Gets the bigram prob.
	 *
	 * @return the bigram prob
	 */
	public double getBigramProb()
	{
		return m_bigramProb;
	}
	
	/**
	 * 返回所有满足的bigram信息.
	 *
	 * @return the features
	 */
	public List<Integer> getFeatures()
	{
		return m_features;
	}
	
}
